package ru.darujo.dto.calendar;

import ru.darujo.type.TypeEnum;

import java.util.Arrays;

public enum DayTypeDto implements TypeEnum {
    HOLIDAY("HOLIDAY", "Праздник"),
    WEEK_END("WEEK_END", "Выходной"),
    VACATION("VACATION", "Отпуск", true),

    WORKDAY("WORKDAY", "Рабочий"),
    SHORTDAY("SHORTDAY", "Сокращенный");

    private final String type;
    private final String name;
    private final boolean user;

    DayTypeDto(String type, String name) {
        this(type, name, false);
    }

    DayTypeDto(String type, String name, boolean user) {
        this.type = type;
        this.name = name;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isUser() {
        return user;
    }

    public static DayTypeDto[] getTypeDay() {
        return Arrays.stream(DayTypeDto.values()).filter(dayTypeDto -> !dayTypeDto.user).toArray(DayTypeDto[]::new);
    }
}
