package ru.darujo.dto.user;

import java.io.Serializable;

public class RightDto implements Serializable {
    private Long id;
    private String code;
    private String name;

    public RightDto() {
    }

    public RightDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
