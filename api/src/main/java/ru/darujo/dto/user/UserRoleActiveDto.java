package ru.darujo.dto.user;


public class UserRoleActiveDto extends RoleDto{
    private Boolean active;

    @SuppressWarnings("unused")
    public UserRoleActiveDto() {
    }

    public UserRoleActiveDto(Long id, String code, String name, Boolean active) {
        super(id, code, name, null);
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
