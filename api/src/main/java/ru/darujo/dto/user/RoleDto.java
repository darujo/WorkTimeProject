package ru.darujo.dto.user;

import java.io.Serializable;

public class RoleDto implements Serializable {
    private Long id;
    private String code;
    private String name;
    private Long projectId;

    public RoleDto() {
    }

    public RoleDto(Long id, String code, String name, Long projectId) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.projectId = projectId;
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

    public Long getProjectId() {
        return projectId;
    }

}
