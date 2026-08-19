package ru.darujo.type;

import java.io.Serializable;

public enum ReportType implements Serializable, TypeEnum {

    ZI_WORK("Работы по ЗИ"),
    ZI_WORK_PROJECT("Работы по ЗИ по проектам", ReportType.ZI_WORK),
    USER_WORK("Работы сотрудников за последние 7 дней"),
    ZI_STATUS("Статус ЗИ"),
    ZI_STATUS_PROJECT("Статус ЗИ по проектам", ReportType.ZI_STATUS),
    ALL_REPORT("Отчеты одним файлом");

    private final String name;
    private final ReportType parentType;
    private final boolean project;
    private MessageType messageType;

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    ReportType(String name) {
        this.name = name;
        this.project = false;
        this.messageType = null;
        this.parentType = null;
    }

    ReportType(String name, ReportType parentType) {
        this.name = name;
        this.project = true;
        this.parentType = parentType;
        this.messageType = null;
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

    public MessageType getMessageType() {
        return messageType;
    }
}
