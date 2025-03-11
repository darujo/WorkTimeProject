package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;
import java.util.Map;

/**
 * Интерфейс для производственного календаря
 */
public interface ProductionCalendarDaysInterface {

    void init();

    void add(String date, DayType type);

    void add(String date, DayType type, String title);

    Map<LocalDate, DateInfo> getDays();

}
