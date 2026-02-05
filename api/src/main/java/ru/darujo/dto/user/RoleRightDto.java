package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoleRightDto extends RoleDto implements Serializable {
    @SuppressWarnings("unused")
    public RoleRightDto() {
        // todo надо убрать наверно
        rights = new ArrayList<>();
    }

    private List<RoleRightActiveDto> rights;

    public RoleRightDto(Long id, String code, String name, List<RoleRightActiveDto> roles) {
        super(id, code, name, null);
        this.rights = roles;
    }

    public Collection<RoleRightActiveDto> getRights() {
        return rights;
    }
}
