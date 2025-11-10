package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UserInfoTypeDto extends UserDto implements Serializable {
    @SuppressWarnings("unused")
    public UserInfoTypeDto() {
        infoTypes= new ArrayList<>();
    }

    private final Collection<UserInfoTypeActiveDto> infoTypes ;

    public UserInfoTypeDto(Long id, String nikName, String firstName, String lastName, String patronymic, Collection<UserInfoTypeActiveDto> roles) {
        super(id, nikName, firstName, lastName, patronymic);
        this.infoTypes = roles;
    }

    public Collection<UserInfoTypeActiveDto> getInfoTypes() {
        return infoTypes;
    }
}
