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
    public boolean init() {
        boolean loadOk = false;
        try {
            messageTypeListMap = userServiceIntegration.getUserMessageDTOs().getMessageTypeListMap();

            loadOk = true;
            updateAllNoAddUser();
            sendAllNotSendMessage();
        } catch (ResourceNotFoundRunTime exception) {
            System.out.println(exception.getMessage());

        }

        return loadOk;
    }

    public void setMessageTypeListMap(MapUserInfoDto messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap.getMessageTypeListMap();
    }

    public Boolean addMessage(MessageInfoDto messageInfoDto) {
        if (messageTypeListMap == null) {
            if (init()) {
                updateAllNoAddUser();
            }


        }
        if (messageTypeListMap == null) {
            saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), false));
        } else {
            MessageInformation messageInformation = saveMessageInformation(new MessageInformation(null, messageInfoDto.getAuthor(), messageInfoDto.getType().toString(), messageInfoDto.getText(), true));
            messageTypeListMap.get(messageInfoDto.getType()).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()), messageInformation)));
        }
        //ToDo пока отправляем сразу надо сделать отложеную отправку
        sendAllNotSendMessage();
        return true;

    }

    private void updateAllNoAddUser() {
        Specification<MessageInformation> specification = Specifications.ne(null, "isSend", false);
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


}
