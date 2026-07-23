package ru.darujo.dto.information;

import ru.darujo.type.MessageSenderType;

public interface SendServiceInt {
    MessageSenderType getMessageSenderType();
    boolean sendMessage(SendMessage sendMessage) throws RuntimeException;
}
