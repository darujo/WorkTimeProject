package ru.darujo.service;


import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.TelegramServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.MessageInformation;
import ru.darujo.model.UserSend;
import ru.darujo.repository.MessageInformationRepository;
import ru.darujo.repository.UserSendRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
@Slf4j
@Service
@Primary
public class MessageInformationService {
    private UserServiceIntegration userServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    private MessageInformationRepository messageInformationRepository;

    @Autowired
    public void setMessageInformationRepository(MessageInformationRepository workTypeRepository) {
        this.messageInformationRepository = workTypeRepository;
    }

    private UserSendRepository userSendRepository;

    @Autowired
    public void setUserSendRepository(UserSendRepository userSendRepository) {
        this.userSendRepository = userSendRepository;
    }

    private TelegramServiceIntegration telegramServiceIntegration;

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        this.telegramServiceIntegration = telegramServiceIntegration;
    }

    Map<MessageType, List<UserInfoDto>> messageTypeListMap = null;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("init mess");
        if (messageTypeListMap == null) {
            try {
                messageTypeListMap = userServiceIntegration.getUserMessageDTOs().getMessageTypeListMap();
            } catch (ResourceNotFoundRunTime exception) {
                log.error(exception.getMessage());
            }
        }
        if (messageTypeListMap != null && availInformNoAddUser()) {
            updateAllNoAddUser();
        }
        if (availNotSendMessage()) {
            sendAllNotSendMessage();
        }


    }

    public void setMessageTypeListMap(MapUserInfoDto messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap.getMessageTypeListMap();
    }

    @Transactional
    public Boolean addMessage(MessageInfoDto messageInfoDto) {
        if (messageTypeListMap == null) {
            init();
        }
        if (messageInfoDto.getUserInfoDto() != null) {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime()));
            saveUserSend(new UserSend(Long.toString(messageInfoDto.getUserInfoDto().getTelegramId()), messageInformation));
        } else if (messageTypeListMap == null) {
            saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), false, messageInfoDto.getDataTime()));
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime()));
            messageTypeListMap.get(messageInfoDto.getType()).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()), messageInformation)));
        }
        sendAllNotSendMessage();
        return true;

    }

    public boolean availInformNoAddUser() {
        Specification<@NonNull MessageInformation> specification = Specifications.eq(null, "isSend", false);
        return messageInformationRepository.exists(specification);

    }

    @Transactional
    public void updateAllNoAddUser() {
        Specification<@NonNull MessageInformation> specification = Specifications.eq(null, "isSend", false);
        messageInformationRepository
                .findAll(specification)
                .forEach(messageInformation -> {
                    messageTypeListMap.get(MessageType.valueOf(messageInformation.getType())).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()), messageInformation)));
                    messageInformation.setSend(true);
                    saveMessageInformation(messageInformation);
                });

    }

    private void saveUserSend(UserSend userSend) {
        userSendRepository.save(userSend);
    }


    public MessageInformation saveMessageInformation(MessageInformation messageInformation) {
        return messageInformationRepository.save(messageInformation);
    }

    public boolean availNotSendMessage() {
        Specification<@NonNull UserSend> specification = Specifications.ne(null, "send", true);
        return userSendRepository.exists(specification);
    }

    @Transactional
    public void sendAllNotSendMessage() {
        Specification<@NonNull UserSend> specification = Specifications.ne(null, "send", true);
        userSendRepository.findAll(specification).forEach(
                userSend -> {
                    try {
                        telegramServiceIntegration.sendMessage(userSend.getMessageInformation().getAuthor(), userSend.getChatId(), userSend.getMessageInformation().getText());
                        userSend.setSend(true);
                        userSendRepository.save(userSend);
                    } catch (ResourceNotFoundRunTime exception) {
                        log.error(exception.getMessage());
                    }

                });
    }


    public List<UserInfoDto> getUsersForMesType(MessageType type) {
        if (messageTypeListMap != null) {
            return messageTypeListMap.get(type);
        }
        return null;

    }

    @Transactional
    public Boolean sendFile(MessageInfoDto messageInfoDto, String fileName, String fileBody) {
        if (messageTypeListMap == null) {
            init();
        }
        if (messageInfoDto.getUserInfoDto() != null && messageTypeListMap == null) {
            log.error("Нет списка получателей");
            return false;
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime()));
            if(messageInfoDto.getUserInfoDto() != null) {
                saveUserSend(new UserSend(Long.toString(messageInfoDto.getUserInfoDto().getTelegramId()), messageInformation));
            } else {
                messageTypeListMap.get(messageInfoDto.getType()).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()), messageInformation)));
            }
            try {
                telegramServiceIntegration.addFile(fileName, fileBody);
                AtomicReference<Boolean> flagError = new AtomicReference<>(false);
                userSendRepository.findAll(
                        Specifications.eq(null,"messageInformation",messageInformation.getId())
                        )
                        .forEach(userSend -> {
                            try {
                                telegramServiceIntegration.sendFile(userSend.getMessageInformation().getAuthor(), userSend.getChatId(), fileName, userSend.getMessageInformation().getText());
                                userSend.setSend(true);
                                userSendRepository.save(userSend);
                            } catch (ResourceNotFoundRunTime ex){
                                log.error("Сбой отправки файла пользователю с chatId {}", userSend.getChatId());
                                flagError.set(true);
                            }
                        });
                if (flagError.get()){
                    messageInformation.setText("Не удалось доставить до вас файл ранее. " + messageInformation.getText());
                    messageInformationRepository.save(messageInformation);
                }
            }
            catch (ResourceNotFoundRunTime ex){
                messageInformation.setText("Не удалось доставить до вас файл ранее. " + messageInformation.getText());
                log.error("Сбой при отправке файла {} {}", fileName, ex.getMessage());
                return false;
            }
            finally {
                try {
                    telegramServiceIntegration.deleteFile(fileName);
                }catch (ResourceNotFoundRunTime ex) {
                    log.error("Сбой при удаление файл из сервиса {} {}", fileName, ex.getMessage());
                }
            }

        }
        sendAllNotSendMessage();
        return true;

    }
}
