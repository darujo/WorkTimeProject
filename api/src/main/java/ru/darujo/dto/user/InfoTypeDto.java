package ru.darujo.dto.user;

import java.io.Serializable;

public class InfoTypeDto implements Serializable {
    private String code;
    private String name;

    public InfoTypeDto() {
    }

    public InfoTypeDto(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
