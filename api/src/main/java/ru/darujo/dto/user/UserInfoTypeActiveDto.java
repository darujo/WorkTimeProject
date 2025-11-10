package ru.darujo.dto.user;

public class UserInfoTypeActiveDto extends InfoTypeDto{
    private Boolean active;

    public UserInfoTypeActiveDto() {
    }

    public UserInfoTypeActiveDto(String code, String name, Boolean active) {
        super( code, name);
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
