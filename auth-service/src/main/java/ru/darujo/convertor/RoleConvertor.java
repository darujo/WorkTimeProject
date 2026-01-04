package ru.darujo.convertor;

import ru.darujo.dto.user.RoleDto;
import ru.darujo.dto.user.UserRoleActiveDto;
import ru.darujo.model.Role;
import ru.darujo.service.ProjectService;

public class RoleConvertor {

    public static UserRoleActiveDto getUserRoleActiveDto(Role role, Boolean active){
        return new UserRoleActiveDto(role.getId(), role.getName(),role.getLabel(),active);
    }

    public static Role getRole(UserRoleActiveDto role){
        return new Role(role.getId(), role.getCode(), role.getName(), null);
    }
    public static Role getRole(RoleDto role){
        return new Role(role.getId(), role.getCode(), role.getName(), ProjectService.getInstance().findById(role.getProjectId()));
    }
    public static RoleDto getRoleDto(Role role){
        return new RoleDto(role.getId(), role.getName(), role.getLabel(), role.getProject().getId());
    }
}
