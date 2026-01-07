package ru.darujo.convertor;

import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserEditDto;
import ru.darujo.model.Project;
import ru.darujo.model.User;
import ru.darujo.service.ProjectService;

public class UserConvertor {
    public static UserDto getUserDto(User user) {
        return new UserDto(user.getId(),
                user.getNikName(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getTelegramId() != null,
                user.getCurrentProject().getId(),
                user.getProjects().stream().map(ProjectConvertor::getProjectDto).toList(),
                user.isBlock()
        );
    }

    public static UserEditDto getUserEditDto(User user) {
        return new UserEditDto(user.getId(),
                user.getNikName(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPassword(),
                user.getPasswordChange(),
                user.getProjects().stream().map(Project::getId).toList(),
                user.isBlock());
    }

    public static User getUser(UserEditDto user) {
        return new User(user.getId(),
                user.getNikName(),
                user.getUserPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getProjects().stream().map(ProjectService.getInstance()::findById).toList(),
                user.isBlock() != null && user.isBlock());
    }

}
