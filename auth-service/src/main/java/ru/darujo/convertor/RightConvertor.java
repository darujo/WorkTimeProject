package ru.darujo.convertor;

import ru.darujo.dto.user.RoleRightActiveDto;
import ru.darujo.model.Right;

public class RightConvertor {

    public static RoleRightActiveDto getRoleRightActiveDto(Right right, Boolean active){
        return new RoleRightActiveDto(right.getId(), right.getName(),right.getLabel(),active);
    }

    public static Right getRight(RoleRightActiveDto right){
        return new Right(right.getId(), right.getCode(),right.getName());
    }
}
