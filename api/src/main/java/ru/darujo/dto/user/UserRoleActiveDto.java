package ru.darujo.dto.user;

import java.util.Objects;

public class UserRoleActiveDto extends RoleDto{
    private Boolean active;

    public UserRoleActiveDto() {
    }

    public UserRoleActiveDto(Long id, String code, String name, Boolean active) {
        super(id, code, name);
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
