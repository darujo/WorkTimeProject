package ru.darujo.convertor;

import ru.darujo.dto.UserDto;
import ru.darujo.model.User;

public class UserConvertor {
    public static UserDto getUserDto(User user){
        return new UserDto(user.getId(),
                           user.getNikName(),
                           user.getFirstName(),
                           user.getLastName(),
                           user.getPatronymic());
    }
}
