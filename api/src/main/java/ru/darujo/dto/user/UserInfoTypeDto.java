package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.Collection;

public class UserInfoTypeDto extends UserDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoTypeDto() {

    }

    private Collection<UserInfoTypeActiveDto> infoTypes = null;
    private Collection<UserInfoTypeActiveDto> infoProjectTypes = null;

    public UserInfoTypeDto(Long id, String nikName, String firstName, String lastName, String patronymic, Collection<UserInfoTypeActiveDto> infoTypes, Collection<UserInfoTypeActiveDto> infoProjectTypes) {
        super(id, nikName, firstName, lastName, patronymic, false);
        this.infoTypes = infoTypes;
        this.infoProjectTypes = infoProjectTypes;
    }

    public Collection<UserInfoTypeActiveDto> getInfoTypes() {
        return infoTypes;
    }

    public Collection<UserInfoTypeActiveDto> getInfoProjectTypes() {
        return infoProjectTypes;
    }
}
