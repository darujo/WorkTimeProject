package ru.darujo.dto.user;

import java.io.Serializable;

public class UserInfoDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoDto() {
    }

    private Long id;

    private String nikName;

    private String telegramId;
    private Integer threadId;
    private Integer originMessageId;

    public UserInfoDto(Long id, String nikName, String telegramId, Integer threadId, Integer originMessageId) {
        this.id = id;
        this.nikName = nikName;
        this.telegramId = telegramId;
        this.threadId = threadId;
        this.originMessageId = originMessageId;
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
}
