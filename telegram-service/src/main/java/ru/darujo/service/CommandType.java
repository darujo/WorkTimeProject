package ru.darujo.service;

public enum CommandType {
    LINK("Привязать акаунт"),
    STOP ("Убрать оповещения"),
    REPORT ("Отчеты"),
    WORK_STATUS ("Статсус задач"),
    WORK_STATUS_ME("Мне"),
    WORK_STATUS_ALL("Подписаным на уведомления"),
    CANCEL("Отменить");


    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
