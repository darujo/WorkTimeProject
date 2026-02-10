package ru.darujo.model;

import java.util.List;

public class WorkTimeTypeDto {
    List<Long> projectList;
    Integer code;
    String name;
    boolean develop;

    public WorkTimeTypeDto(List<Long> projectList, Integer code, String name, boolean develop) {
        this.projectList = projectList;
        this.code = code;
        this.name = name;
        this.develop = develop;
    }

    public List<Long> getProjectList() {
        return projectList;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isDevelop() {
        return develop;
    }
}
