package ru.darujo.service;

import lombok.Getter;

@Getter
public enum CommandType {
    LINK("Привязать акаунт"),
    STOP ("Убрать оповещения"),
    REPORT ("Отчеты"),
    WORK_STATUS ("Статус задач"),
    WORK_STATUS_ME("Мне"),
    WORK_STATUS_ALL("Подписаным на уведомления"),
    CANCEL("Отменить");


    private final String name;

    CommandType(String name) {
        this.name = name;
    }

}
