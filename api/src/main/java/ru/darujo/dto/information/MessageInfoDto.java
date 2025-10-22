package ru.darujo.dto.information;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageInfoDto implements Serializable {
    Timestamp dataTime;
    String author;
    MessageType type;
    String text;

    public MessageInfoDto(Timestamp dataTime, String author, MessageType type, String text) {
        this.dataTime = dataTime;
        this.author = author;
        this.type = type;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Timestamp getDataTime() {
        return dataTime;
    }
}
