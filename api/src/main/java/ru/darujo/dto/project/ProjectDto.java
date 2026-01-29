package ru.darujo.dto.project;

import java.io.Serializable;

public class ProjectDto implements Serializable {

    private Long id;
    private String code;
    private String name;

    @SuppressWarnings("unused")
    public ProjectDto() {
    }

    public ProjectDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }
}
