package ru.darujo.dto.information;

public interface UserSendMessage {


    String getChatId();

    Integer getThreadId();

    Integer getOriginMessageId();

    void setSend();
}
