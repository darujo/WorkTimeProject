package ru.darujo.model;

import lombok.Getter;

import java.util.List;

public class WorkTimeTypeDto {
    @Getter
    private final List<Long> projectList;
    @Getter
    private final Integer code;
    @Getter
    private final String name;
    @Getter
    private final boolean develop;
    @Getter
    private final List<Long> projectNotList;

    public WorkTimeTypeDto(List<Long> projectList, Integer code, String name, boolean develop, List<Long> projectNotList) {
        this.projectList = projectList;
        this.code = code;
        this.name = name;
        this.develop = develop;
        this.projectNotList = projectNotList;
    }
    public WorkTimeTypeDto(List<Long> projectList, Integer code, String name, boolean develop) {
        this.projectList = projectList;
        this.code = code;
        this.name = name;
        this.develop = develop;
        projectNotList = null;

    }

}
