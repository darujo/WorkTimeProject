package ru.darujo.dto.information;

import ru.darujo.dto.user.UserInfoDto;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageInfoDto implements Serializable {
    @SuppressWarnings("unused")
    public MessageInfoDto() {
    }

    private Timestamp dataTime;
    private String author;
    private MessageType type;
    private String text;
    private UserInfoDto userInfoDto;

    public MessageInfoDto(String author,UserInfoDto userInfoDto, MessageType type, String text) {
        this(userInfoDto,type,text);
        if (author != null) {
            this.author = author;
            this.text = this.author + ": " + text;
        }
    }
    public MessageInfoDto(UserInfoDto userInfoDto, MessageType type, String text) {
        this.dataTime = new Timestamp(System.currentTimeMillis());
        this.author = "Scheduler";
        this.type = type;
        this.text = text;
        this.userInfoDto = userInfoDto;
    }

    public MessageInfoDto(Timestamp dataTime, String author, MessageType type, String text) {
        this.dataTime = dataTime;
        this.author = author;
        this.type = type;
        this.text = text;
    }

    @SuppressWarnings("unused")
    public String getAuthor() {
        return author;
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("unused")
    public Timestamp getDataTime() {
        return dataTime;
    }

    public void setDataTime(Timestamp dataTime) {
        this.dataTime = dataTime;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public UserInfoDto getUserInfoDto() {
        return userInfoDto;
    }
}
