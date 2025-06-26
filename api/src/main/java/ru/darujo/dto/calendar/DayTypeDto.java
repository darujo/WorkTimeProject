package ru.darujo.dto.calendar;

public enum DayTypeDto {
    HOLIDAY("HOLIDAY"),
    WEEK_END("WEEK_END"),
    VACATION("VACATION"),

    WORKDAY("WORKDAY"),
    SHORTDAY("SHORTDAY");

    private final String type;

    DayTypeDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
