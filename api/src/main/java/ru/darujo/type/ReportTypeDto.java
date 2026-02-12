package ru.darujo.type;

import java.io.Serializable;

public enum ReportTypeDto implements Serializable,TypeEnum {

    ZI_WORK("Работы по ЗИ"),
    ZI_WORK_PROJECT("Работы по ЗИ по проектам"),
    USER_WORK("Работы сотрудников за предыдущую неделю"),
    ZI_STATUS("Статус ЗИ"),
    ZI_STATUS_PROJECT("Статус ЗИ по проектам");

    private final String name;

    ReportTypeDto(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
