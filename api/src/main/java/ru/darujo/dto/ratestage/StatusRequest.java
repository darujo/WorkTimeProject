package ru.darujo.dto.ratestage;

import ru.darujo.type.TypeEnum;

public enum StatusRequest implements TypeEnum {
    REQUEST("Запрошено Внутренее согласование"),
    SENT_CUSTOMER("Отправлено на согласование заказчику");

    StatusRequest(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String getName() {
        return name;
    }
}
