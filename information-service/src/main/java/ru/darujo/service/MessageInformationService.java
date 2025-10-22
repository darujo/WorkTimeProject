package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MapUserInfoDto;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.user.UserTelegramDto;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.model.MessageInformation;
import ru.darujo.model.UserSend;
import ru.darujo.repository.MessageInformationRepository;
import ru.darujo.repository.UserSendRepository;

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

    Map<MessageType, List<UserTelegramDto>> messageTypeListMap = null;

    @PostConstruct
    public boolean init() {
        boolean loadOk = false;
        try {
            messageTypeListMap = userServiceIntegration.getUserMessageDTOs().getMessageTypeListMap();

//            for (MessageType type : MessageType.values()) {
//                List<UserTelegramDto> userDTOs =
//                if(messageTypeListMapLoad == null){
//                    messageTypeListMapLoad = new HashMap<>();
//                }
//                messageTypeListMapLoad.put(type,userDTOs);
//
//            }
            loadOk = true;
        } catch (ResourceNotFoundException exception) {
            System.out.println(exception.getMessage());

        }
       return loadOk;
    }
    //todo Доделать обновление если у пользователя поменялась настройка
    public void setMessageTypeListMap(MapUserInfoDto messageTypeListMap) {
        this.messageTypeListMap = messageTypeListMap.getMessageTypeListMap();
    }

    public Boolean addMessage(MessageInfoDto messageInfoDto) {
        if (messageTypeListMap == null){
            if(init()){
                updateAllNoAddUser();
            }


        }
        if (messageTypeListMap == null){
            saveMessageInformation(new MessageInformation(null,messageInfoDto.getType().toString(), messageInfoDto.getText(), false,null));
        } else {
            MessageInformation messageInformation =saveMessageInformation(new MessageInformation(null,messageInfoDto.getType().toString(), messageInfoDto.getText(), true,null));
            messageTypeListMap.get(messageInfoDto.getType()).forEach(userTelegramDto -> saveUserSend(new UserSend(Long.toString(userTelegramDto.getTelegramId()),messageInformation)));
        }
        return true;

    }

    private void updateAllNoAddUser() {
        // Todo обновить записи по которым список пустой и еще не отправлены

    }

    private void saveUserSend(UserSend userSend) {
        userSendRepository.save(userSend);
    }


    public MessageInformation saveMessageInformation(MessageInformation messageInformation) {
        return messageInformationRepository.save(messageInformation);
    }

    public void deleteMessageInformation(Long id) {
        messageInformationRepository.deleteById(id);
    }

    public  void sendAllNotSendMessage(){
        //TODo доделать отправку сообщений в сервисы
    }


}
