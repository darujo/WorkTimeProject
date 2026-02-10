package ru.darujo.model;

import java.util.List;

public class WorkTimeType {
    List<Long> projectList;
    Long code;
    String name;
    boolean develop;

    public WorkTimeType(List<Long> projectList, Long code, String name, boolean develop) {
        this.projectList = projectList;
        this.code = code;
        this.name = name;
        this.develop = develop;
    }

    public List<Long> getProjectList() {
        return projectList;
    }
}
