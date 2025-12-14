package ru.darujo.dto.user;

import ru.darujo.dto.information.CodeTelegramMes;

public class UserInfoTypeActiveDto extends InfoTypeDto{
    private Boolean active;
    private Long telegramId;
    private Integer threadId;
    private CodeTelegramMes message;

    @SuppressWarnings("unused")
    public UserInfoTypeActiveDto() {
    }

    public UserInfoTypeActiveDto(String code, String name, Boolean active) {
        super( code, name);
        this.active = active;
    }

    @SuppressWarnings("unused")
    public Boolean getActive() {
        return active;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unused")
    public Long getTelegramId() {
        return telegramId;
    }

    @SuppressWarnings("unused")
    public Integer getThreadId() {
        return threadId;
    }

    public void setMessage(CodeTelegramMes message) {
        this.message = message;
    }

    public CodeTelegramMes getMessage() {
        return message;
    }
}
