package ru.darujo.dto;

import lombok.Getter;

public class LinkDto {
    @Getter
    private String href;

    @SuppressWarnings("unused")
    public LinkDto() {
    }

    @SuppressWarnings("unused")
    public LinkDto(String href) {
        this.href = href;
    }
}
