package ru.darujo.model;

import ru.darujo.dto.information.SendMessage;
import ru.darujo.dto.information.UserSendMessage;
import ru.darujo.type.MessageType;

import java.util.Collection;

public class SendMessageImp implements SendMessage {
    MessageInformation messageInformation;
    Collection<UserSend> userSendList;
    String fileName;
    String fileBody;

    public SendMessageImp(MessageInformation messageInformation, Collection<UserSend> userSendList, String fileName, String fileBody) {
        this.messageInformation = messageInformation;
        this.userSendList = userSendList;
        this.fileName = fileName;
        this.fileBody = fileBody;
    }

    @Override
    public String getAuthor() {
        return messageInformation.getAuthor();
    }

    @Override
    public String getTitle() {
        return MessageType.valueOf(messageInformation.getType()).getName();
    }

    @Override
    public String getText() {
        return messageInformation.getText();
    }

    @Override
    public boolean isAttachFile() {
        return fileBody != null && !fileBody.isEmpty();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileBody() {
        return fileBody;
    }

    @Override
    public Collection<? extends UserSendMessage> getUserSendMessages() {
        return userSendList.stream().map(UserSendMessageImp::new).toList();
    }
}
