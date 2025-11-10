package ru.darujo.dto.user;

import java.io.Serializable;

public class UserInfoDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoDto() {
    }

    private Long id;

    private String nikName;

    private Long telegramId;

    public UserInfoDto(Long id, String nikName, Long telegramId) {
        this.id = id;
        this.nikName = nikName;
        this.telegramId = telegramId;
    }

    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public Long getTelegramId() {
        return telegramId;
    }
}
