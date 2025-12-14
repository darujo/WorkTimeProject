package ru.darujo.dto.ratestage;

import ru.darujo.type.TypeEnum;

public enum StatusResponse implements TypeEnum {
    AGREED("Согласовано"),
    ANY_COMMENT("Оставлены замечания");

    StatusResponse(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String getName() {
        return name;
    }

}
