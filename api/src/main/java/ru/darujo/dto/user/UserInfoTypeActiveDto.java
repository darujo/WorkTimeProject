package ru.darujo.dto.user;

import ru.darujo.dto.information.CodeTelegramMes;

public class UserInfoTypeActiveDto extends InfoTypeDto{
    private Long projectId;
    private Boolean active;
    private String telegramId;
    private Integer threadId;
    private CodeTelegramMes message;

    @SuppressWarnings("unused")
    public UserInfoTypeActiveDto() {
    }

    public UserInfoTypeActiveDto(Long projectId, String code, String name, Boolean active) {
        super( code, name);
        this.projectId = projectId;
        this.active = active;
    }

    @SuppressWarnings("unused")
    public Boolean getActive() {
        return active;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unused")
    public String getTelegramId() {
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

    @SuppressWarnings("unused")
    public Long getProjectId() {
        return projectId;
    }
}
