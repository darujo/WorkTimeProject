package ru.darujo.utils.calendar.structure;

/**
 * Типы дат
 */
public enum DayType {
    WEEK_END,

    /**
     * Нерабочий или праздничный день.
     */
    HOLIDAY,

    /**
     * Сокращенный рабочий день.
     */
    SHORTDAY,

    /**
     * Рабочий день.
     */
    WORKDAY
}
