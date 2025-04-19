package ru.darujo.dto.user;

public class RoleRightActiveDto extends RightDto{
    private Boolean active;

    public RoleRightActiveDto() {
    }

    public RoleRightActiveDto(Long id, String code, String name, Boolean active) {
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
