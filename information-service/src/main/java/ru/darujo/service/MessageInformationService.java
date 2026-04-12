package ru.darujo.service;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.TelegramServiceIntegration;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.MessageInformation;
import ru.darujo.model.UserSend;
import ru.darujo.repository.MessageInformationRepository;
import ru.darujo.repository.UserSendRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.type.MessageSenderType;
import ru.darujo.type.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Primary
public class MessageInformationService {
    private UserServiceIntegration userServiceIntegration;
    private MessageInformationRepository messageInformationRepository;
    private UserSendRepository userSendRepository;
    private TelegramServiceIntegration telegramServiceIntegration;
    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @Autowired
    public void setMessageInformationRepository(MessageInformationRepository workTypeRepository) {
        this.messageInformationRepository = workTypeRepository;
    }

    @Autowired
    public void setUserSendRepository(UserSendRepository userSendRepository) {
        this.userSendRepository = userSendRepository;
    }

    @Autowired
    public void setTelegramServiceIntegration(TelegramServiceIntegration telegramServiceIntegration) {
        this.telegramServiceIntegration = telegramServiceIntegration;
    }

    Map<MessageType, Map<MessageSenderType, List<UserInfoDto>>> messageTypeListMap = null;
    private Timestamp lastInitMesType;

    @Transactional
    public void initMesType() {
        log.info("init mess");
        if (messageTypeListMap == null
                || lastInitMesType == null
                || lastInitMesType.before(new Timestamp(System.currentTimeMillis()))) {
            lastInitMesType = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000);
            try {
                messageTypeListMap = userServiceIntegration.getUserMessageDTOs().getMessageTypeListMap();
                log.info("init mess ok");
            } catch (ResourceNotFoundRunTime exception) {
                log.error(exception.getMessage());
            }
        }
    }

    @Transactional
    public boolean sendMesNotSend() {
        boolean flagOk = false;
        if (availInformNoAddUser()) {
            if (messageTypeListMap != null) {
                updateAllNoAddUser();
                flagOk = true;
            }
        } else {
            flagOk = true;
        }
        if (availNotSendMessage()) {
            if (!sendAllNotSendMessage()) {
                flagOk = false;
            }
        }
        return flagOk;
    }

    public void setMessageTypeListMap(MapUserInfoDto messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap.getMessageTypeListMap();
    }
    @Transactional
    public Boolean addMessage(MessageInfoDto messageInfoDto) {
        return addMessage(messageInfoDto, null);
    }

    @Transactional
    public Boolean addMessage(MessageInfoDto messageInfoDto, List<UserInfoDto> userSendList) {
        initMesType();

        if (messageInfoDto.getText().isBlank()) {
            return false;
        }
        if (userSendList != null) {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime(), null));
            userSendList.forEach(userInfoDto ->
                    saveUserSend(new UserSend(
                            userInfoDto.getSenderType(),
                            userInfoDto.getTelegramId(),
                            userInfoDto.getThreadId(),
                            userInfoDto.getOriginMessageId(),
                            messageInformation))
            );
        } else if (messageInfoDto.getUserInfoDto() != null) {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime(), null));
            saveUserSend(new UserSend(
                    messageInfoDto.getUserInfoDto().getSenderType(),
                    messageInfoDto.getUserInfoDto().getTelegramId(),
                    messageInfoDto.getUserInfoDto().getThreadId(),
                    messageInfoDto.getUserInfoDto().getOriginMessageId(),
                    messageInformation));
        } else if (messageTypeListMap == null) {
            saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), false, messageInfoDto.getDataTime(), null));
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true, messageInfoDto.getDataTime(), null));
            messageTypeListMap.get(messageInfoDto.getType()).forEach((senderType, userInfoDTOList) ->
                    userInfoDTOList.forEach(userInfoDto ->
                            saveUserSend(new UserSend(userInfoDto.getSenderType(), userInfoDto.getTelegramId(), userInfoDto.getThreadId(), null, messageInformation))));
        }
        if (sendAllNotSendMessage()) {
            ScheduleService.getINSTANCE().sendMes();
        }
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
                    messageTypeListMap.get(MessageType.valueOf(messageInformation.getType()))
                            .forEach((senderType, userInfoDTOs) ->

                                    userInfoDTOs.stream().filter(userInfoDto ->
                                                    (messageInformation.getProjectId() == null
                                                            && userInfoDto.getProjectId() == null)
                                                            ||
                                                            (messageInformation.getProjectId() != null
                                                                    && (messageInformation.getProjectId().equals(userInfoDto.getProjectId())
                                                                    || userInfoDto.getProjectId() == null)))

                                            .forEach(userInfoDto -> saveUserSend(new UserSend(senderType.toString(),
                                                    userInfoDto.getTelegramId(),
                                                    userInfoDto.getThreadId(),
                                                    userInfoDto.getOriginMessageId(),
                                                    messageInformation))));
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
    public boolean sendAllNotSendMessage() {
        Specification<@NonNull UserSend> specification = Specifications.ne(null, "send", true);
        AtomicBoolean flagOk = new AtomicBoolean(true);
        userSendRepository.findAll(specification).forEach(
                userSend -> {
                    try {
                        if (userSend.getMessageInformation().getFileForDisk() != null) {
                            String body = fileService.getFileBody(pathSave + userSend.getMessageInformation().getFileForDisk());
                            if (body != null && !body.isBlank()) {
                                telegramServiceIntegration.sendFile(
                                        userSend.getMessageInformation().getAuthor(),
                                        userSend.getChatId(),
                                        userSend.getThreadId(),
                                        userSend.getOriginMessageId(),
                                        userSend.getMessageInformation().getFileForDisk(),
                                        userSend.getMessageInformation().getText(),
                                        body);
                            }
                        } else {
                            telegramServiceIntegration.sendMessage(
                                    userSend.getMessageInformation().getAuthor(),
                                    userSend.getChatId(),
                                    userSend.getThreadId(),
                                    userSend.getMessageInformation().getText());
                        }
                        userSend.setSend(true);
                        userSendRepository.save(userSend);
                    } catch (ResourceNotFoundRunTime exception) {
                        flagOk.set(false);
                        log.error(exception.getMessage(), exception);
                    }

                });
        return flagOk.get();
    }


    public Map<String, List<UserInfoDto>> getUsersForMesType(MessageType type) {
        if (messageTypeListMap != null) {
            Map<String, List<UserInfoDto>> userMap = new HashMap<>();

            messageTypeListMap
                    .get(type)
                    .forEach((senderType, userInfoDTOs) ->
                            userInfoDTOs.forEach(userInfoDto -> {
                                List<UserInfoDto> userList = userMap.computeIfAbsent(userInfoDto.getNikName(), s -> new ArrayList<>());
                                userList.add(userInfoDto);
                            }));
        }
        return null;

    }

    @Transactional
    public Boolean sendFile(MessageInfoDto messageInfoDto, String fileName, String fileBody) {
        return sendFile(messageInfoDto, null, fileName, fileBody);
    }

    @Transactional
    public Boolean sendFile(MessageInfoDto messageInfoDto, Long projectId, String fileName, String fileBody) {
        if (messageTypeListMap == null) {
            initMesType();
        }
        if (messageInfoDto.getUserInfoDto() != null && messageTypeListMap == null) {
            log.error("Нет списка получателей");
            return false;
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), false, messageInfoDto.getDataTime(), projectId));
            if (messageInfoDto.getUserInfoDto() != null) {
                saveUserSend(new UserSend(
                        messageInfoDto.getUserInfoDto().getSenderType(),
                        messageInfoDto.getUserInfoDto().getTelegramId(),
                        messageInfoDto.getUserInfoDto().getThreadId(),
                        messageInfoDto.getUserInfoDto().getOriginMessageId(),
                        messageInformation));
                messageInformation.setSend(true);
                saveMessageInformation(messageInformation);
            } else {
                updateAllNoAddUser();
            }
            boolean flagSendFile = false;
            try {
                telegramServiceIntegration.addFile(fileName, fileBody);
                flagSendFile = true;
                AtomicReference<Boolean> flagError = new AtomicReference<>(false);
                userSendRepository.findAll(
                                Specifications.eq(null, "messageInformation", messageInformation)
                        )
                        .forEach(userSend -> {
                            try {
                                telegramServiceIntegration.sendFile(userSend.getMessageInformation().getAuthor(), userSend.getChatId(), userSend.getThreadId(), userSend.getOriginMessageId(), fileName, userSend.getMessageInformation().getText());
                                userSend.setSend(true);
                                userSendRepository.save(userSend);
                            } catch (ResourceNotFoundRunTime ex) {
                                log.error("Сбой отправки файла пользователю с chatId {}", userSend.getChatId());
                                flagError.set(true);
                            }
                        });
                if (flagError.get()) {
                    saveFile(messageInformation, fileName, fileBody);
                }
            } catch (ResourceNotFoundRunTime ex) {
                saveFile(messageInformation, fileName, fileBody);
                log.error("Сбой при отправке файла {} {}", fileName, ex.getMessage());
                return false;
            } finally {
                if (flagSendFile) {
                    try {
                        telegramServiceIntegration.deleteFile(fileName);
                    } catch (ResourceNotFoundRunTime ex) {
                        log.error("Сбой при удаление файл из сервиса {} {}", fileName, ex.getMessage(), ex);
                    }
                }
            }

        }
        if (sendAllNotSendMessage()) {
            ScheduleService.getINSTANCE().sendMes();
        }
        return true;

    }

    @Value("${file.save-into}")
    private String pathSave;

    private void saveFile(MessageInformation messageInformation, String file, String body) {
        fileService.saveFile(pathSave + file, body);
        messageInformation.setText("Не удалось доставить до вас файл ранее. " + messageInformation.getText());
        messageInformation.setFileForDisk(file);
        messageInformationRepository.save(messageInformation);
        ScheduleService.getINSTANCE().sendMes();
    }
}
