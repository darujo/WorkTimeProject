package ru.darujo.type;

import java.io.Serializable;

public enum ReportTypeDto implements Serializable,TypeEnum {
    ZI_WORK("Работы по ЗИ"),
    USER_WORK("Работы сотрудников за предыдущую неделю"),
    ZI_STATUS("Статус задач");

    private final String name;

    ReportTypeDto(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
