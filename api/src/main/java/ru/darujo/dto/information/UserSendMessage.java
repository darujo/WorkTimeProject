package ru.darujo.dto.information;

public interface UserSendMessage {


    String getChatId();

    Integer getThreadId();

    String getOriginMessageId();

    void setSend();
}
