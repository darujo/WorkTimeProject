package ru.darujo.dto.user;

import java.io.Serializable;

public class UserInfoDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoDto() {
    }

    private Long id;

    private String nikName;
    private Long projectId;
    private String senderType;
    private String telegramId;
    private Integer threadId;
    private Integer originMessageId;

    public UserInfoDto(String senderType, Long id, String nikName, String telegramId, Integer threadId, Integer originMessageId) {
        this.id = id;
        this.nikName = nikName;
        this.telegramId = telegramId;
        this.threadId = threadId;
        this.originMessageId = originMessageId;
        this.senderType = senderType;
    }

    public UserInfoDto(String senderType, Long id, String nikName, Long projectId, String telegramId, Integer threadId, Integer originMessageId) {
        this.id = id;
        this.nikName = nikName;
        this.projectId = projectId;
        this.telegramId = telegramId;
        this.threadId = threadId;
        this.originMessageId = originMessageId;
        this.senderType = senderType;
    }

    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public Integer getOriginMessageId() {
        return originMessageId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getSenderType() {
        return senderType;
    }
}
