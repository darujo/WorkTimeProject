package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ChatInfo {

    @Setter
    private String author;
    @Setter
    private String chatId;
    @Setter
    private Integer threadId;
    @Setter
    private Integer originMessageId;

}
