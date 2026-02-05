package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.Collection;

public class UserRoleDto extends UserDto implements Serializable {
    @SuppressWarnings("unused")
    public UserRoleDto() {

    }

    private Collection<UserRoleActiveDto> roles;

    public UserRoleDto(Long id, String nikName, String firstName, String lastName, String patronymic, Collection<UserRoleActiveDto> roles) {
        super(id, nikName, firstName, lastName, patronymic, false);
        this.roles = roles;
    }

    public Collection<UserRoleActiveDto> getRoles() {
        return roles;
    }
}
