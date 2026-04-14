package ru.darujo.model;

import ru.darujo.dto.information.UserSendMessage;

public class UserSendMessageImp implements UserSendMessage {
    private final UserSend userSend;

    public UserSendMessageImp(UserSend userSend) {
        this.userSend = userSend;
    }

    @Override
    public String getChatId() {
        return userSend.getChatId();
    }

    @Override
    public Integer getThreadId() {
        return userSend.getThreadId();
    }

    @Override
    public Integer getOriginMessageId() {
        return userSend.getOriginMessageId();
    }

    @Override
    public void setSend() {
       userSend.setSend(true);
    }
}
