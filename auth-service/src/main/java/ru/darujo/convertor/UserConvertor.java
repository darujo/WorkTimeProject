package ru.darujo.convertor;

import org.hibernate.LazyInitializationException;
import ru.darujo.dto.user.UserDto;
import ru.darujo.dto.user.UserEditDto;
import ru.darujo.model.Category;
import ru.darujo.model.Project;
import ru.darujo.model.User;
import ru.darujo.service.CategoryService;
import ru.darujo.service.ProjectService;

public class UserConvertor {
    public static UserDto getUserDto(User user) {
        String name = null;
        Long id = null;
        try {
            Category category = user.getCategory();
            if (category != null) {
                id = category.getId();
                name = category.getName();
            }
        } catch (LazyInitializationException ignore) {

        }
        return new UserDto(user.getId(),
                user.getNikName(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getTelegramId() != null,
                user.getMaxId() != null,
                user.getCurrentProject().getId(),
                user.getProjects().stream().map(ProjectConvertor::getProjectDto).toList(),
                user.isBlock(),
                id,
                name
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
                user.isBlock(),
                user.getRights() == null ? null : user.getRights().stream().anyMatch(right -> right.getName().equals("ADMIN_USER")),
                user.getEmail(),
                user.getCategory() == null ? null : user.getCategory().getId());
    }

    public static User getUser(UserEditDto user) {
        return new User(user.getId(),
                user.getNikName(),
                user.getUserPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getProjects() == null ? null : user.getProjects().stream().map(ProjectService.getInstance()::findById).toList(),
                user.isBlock() != null && user.isBlock(),
                null,
                null,
                user.getEmail(),
                null,
                null,
                CategoryService.getInstance().findById(user.getCategoryId())
        );
    }

    public static User getUserCopyEmpty(User user) {
        return new User(user.getId(),
                user.getNikName(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getPasswordChange(),
                user.getProjects(),
                user.isBlock(),
                null,
                null,
                user.getEmail(),
                null,
                null,
                user.getCategory()
        );
    }

}
