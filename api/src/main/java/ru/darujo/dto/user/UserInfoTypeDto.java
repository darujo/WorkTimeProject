package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.Collection;

public class UserInfoTypeDto extends UserDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoTypeDto() {

    }

    private Collection<UserInfoTypeActiveDto> infoTypes = null;

    public UserInfoTypeDto(Long id, String nikName, String firstName, String lastName, String patronymic, Collection<UserInfoTypeActiveDto> roles) {
        super(id, nikName, firstName, lastName, patronymic);
        this.infoTypes = roles;
    }

    public Collection<UserInfoTypeActiveDto> getInfoTypes() {
        return infoTypes;
    }
}
