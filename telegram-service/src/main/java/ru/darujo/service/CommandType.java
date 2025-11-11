package ru.darujo.service;

import lombok.Getter;
import ru.darujo.type.TypeEnum;

@Getter
public enum CommandType implements TypeEnum {
    LINK("Привязать акаунт"),
    STOP ("Убрать оповещения"),
    REPORT ("Отчеты",true),
    SEND_ME("Мне"),
    SEND_ALL("Подписаным на уведомления"),
    CANCEL("Отменить");


    private final String name;
    private final Boolean newParam;
    CommandType(String name) {
        this.name = name;
        this.newParam = false;
    }

    CommandType(String name, Boolean newParam) {
        this.name = name;
        this.newParam = newParam;
    }

}
