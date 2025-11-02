package ru.darujo.service;

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

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
    public void init() {
        System.out.println("init mess");
        if (messageTypeListMap == null) {
            try {
                messageTypeListMap = userServiceIntegration.getUserMessageDTOs().getMessageTypeListMap();
            } catch (ResourceNotFoundRunTime exception) {
                System.out.println(exception.getMessage());
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
        if (messageInfoDto.getUserInfoDto()!=null){
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true));
            saveUserSend(new UserSend(Long.toString(messageInfoDto.getUserInfoDto().getTelegramId()), messageInformation));
        }
        else if (messageTypeListMap == null) {
            saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), false));
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true));
            messageTypeListMap.get(messageInfoDto.getType()).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()), messageInformation)));
        }
        //ToDo пока отправляем сразу надо сделать отложеную отправку
        sendAllNotSendMessage();
        return true;

    }

    public boolean availInformNoAddUser() {
        Specification<MessageInformation> specification = Specifications.eq(null, "isSend", false);
        return messageInformationRepository.exists(specification);

    }

    @Transactional
    public void updateAllNoAddUser() {
        Specification<MessageInformation> specification = Specifications.eq(null, "isSend", false);
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
        Specification<UserSend> specification = Specifications.ne(null, "send", true);
        return userSendRepository.exists(specification);
    }

    @Transactional
    public void sendAllNotSendMessage() {
        Specification<UserSend> specification = Specifications.ne(null, "send", true);
        userSendRepository.findAll(specification).forEach(
                userSend -> {
                    try {
                        telegramServiceIntegration.sendMessage(userSend.getMessageInformation().getAuthor(), userSend.getChatId(), userSend.getMessageInformation().getText());
                        userSend.setSend(true);
                        userSendRepository.save(userSend);
                    } catch (ResourceNotFoundRunTime exception) {
                        System.out.println(exception.getMessage());
                    }

                });
    }


    public List<UserInfoDto> getUsersForMesType(MessageType type) {
        if (messageTypeListMap != null) {
            return messageTypeListMap.get(type);
        }
        return null;

    }
}
