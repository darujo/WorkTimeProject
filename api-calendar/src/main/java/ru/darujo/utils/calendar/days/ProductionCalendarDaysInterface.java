package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;

/**
 * Интерфейс для производственного календаря
 */
public interface ProductionCalendarDaysInterface {

    void init();

    default DayType getType(LocalDate date) {
        DateInfo dateInfo = getDateInfo(date);
        if (dateInfo == null) {
            return null;
        }
        return dateInfo.getType();
    }

    boolean isWeekEnd(LocalDate date);

    DateInfo getDateInfo(LocalDate date);
}
