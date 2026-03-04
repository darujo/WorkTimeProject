package ru.darujo.service;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class SingleCode {
    private final String login;
    private final Long projectId;
    private final String messageType;
    private final Timestamp timestamp;

    public SingleCode(String login, Long projectId, String messageType, Timestamp timestamp) {
        this.login = login;
        this.projectId = projectId;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

}
