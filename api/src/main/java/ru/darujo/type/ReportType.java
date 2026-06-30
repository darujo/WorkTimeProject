package ru.darujo.type;

import java.io.Serializable;

public enum ReportType implements Serializable, TypeEnum {

    ZI_WORK("Работы по ЗИ"),
    ZI_WORK_PROJECT("Работы по ЗИ по проектам", ReportType.ZI_WORK),
    USER_WORK("Работы сотрудников за последние 7 дней"),
    ZI_STATUS("Статус ЗИ"),
    ZI_STATUS_PROJECT("Статус ЗИ по проектам", ReportType.ZI_STATUS);

    private final String name;
    private ReportType parentType;
    private final boolean project;

    ReportType(String name) {
        this.name = name;
        this.project = false;
    }

    ReportType(String name, ReportType parentType) {
        this.name = name;
        this.project = true;
        this.parentType = parentType;
    }

    @Override
    public String getName() {
        return name;
    }

    public ReportType getParentType() {
        return parentType;
    }

    public boolean isProject() {
        return project;
    }
}
