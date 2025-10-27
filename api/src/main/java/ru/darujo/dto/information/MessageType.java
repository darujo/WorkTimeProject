package ru.darujo.dto.information;

public enum MessageType {
    SYSTEM_INFO("Системные информационые сообщения"),
    ESTIMATION_WORK("Проведена оценка"),
    CHANGE_STAGE_WORK("Смена статуса ЗИ"),
    UPDATE_INFO("Список испрапрвлений в новой верси ");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
