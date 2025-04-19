package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UserRoleDto extends UserDto implements Serializable {
    public UserRoleDto() {
        roles= new ArrayList<>();
    }

    private final Collection<UserRoleActiveDto> roles ;

    public UserRoleDto(Long id, String nikName, String firstName, String lastName, String patronymic, Collection<UserRoleActiveDto> roles) {
        super(id, nikName, firstName, lastName, patronymic);
        this.roles = roles;
    }

    public Collection<UserRoleActiveDto> getRoles() {
        return roles;
    }
}
