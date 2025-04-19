package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class RoleRightDto extends RoleDto implements Serializable {
    public RoleRightDto() {
        rights = new ArrayList<>();
    }

    private final Collection<RoleRightActiveDto> rights;

    public RoleRightDto(Long id, String code, String name, Collection<RoleRightActiveDto> roles) {
        super(id, code, name);
        this.rights = roles;
    }

    public Collection<RoleRightActiveDto> getRights() {
        return rights;
    }
}
