package ru.darujo.dto.user;

import java.io.Serializable;
import java.util.Objects;

public class RoleDto implements Serializable {
    private Long id;
    private String code;
    private String name;

    public RoleDto() {
    }

    public RoleDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
