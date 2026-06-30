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
    private String originMessageId;

    public ChatInfo(String author, Long chatId, String originMessageId) {
        this.author = author;
        this.chatId = Long.toString(chatId);
        this.originMessageId = originMessageId;
    }
}
