package ru.darujo.model;

public class TaskType {

    private final String name;
    private final boolean isZi;

    public TaskType(String name, boolean isZi) {
        this.name = name;
        this.isZi = isZi;
    }

    public String getName() {
        return name;
    }

    public boolean isZi() {
        return isZi;
    }
}
