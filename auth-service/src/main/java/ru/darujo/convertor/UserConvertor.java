package ru.darujo.convertor;

import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserEditDto;
import ru.darujo.dto.user.UserInfoDto;
import ru.darujo.model.User;

public class UserConvertor {
    public static UserDto getUserDto(User user) {
        return new UserDto(user.getId(),
                user.getNikName(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getTelegramId() != null
        );
    }

    public static UserEditDto getUserEditDto(User user) {
        return new UserEditDto(user.getId(),
                user.getNikName(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPassword(),
                "",
                user.getPasswordChange());
    }

    public static User getUser(UserEditDto user) {
        return new User(user.getId(),
                user.getNikName(),
                user.getUserPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange());
    }

    public static UserInfoDto getUserInfoDto(User user) {
        return new UserInfoDto(user.getId(), user.getNikName(), user.getTelegramId());
    }
}
