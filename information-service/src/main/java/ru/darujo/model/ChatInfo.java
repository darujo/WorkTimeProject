package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.darujo.type.MessageSenderType;

@Getter
@AllArgsConstructor
public class ChatInfo {

    @Setter
    private String author;
    @Setter
    private MessageSenderType senderType;
    @Setter
    private String chatId;
    @Setter
    private Integer threadId;
    @Setter
    private Integer originMessageId;

}
