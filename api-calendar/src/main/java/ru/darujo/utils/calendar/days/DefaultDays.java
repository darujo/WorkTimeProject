package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Производственный календарь по-умолчанию
 */
public class DefaultDays extends DateInfoRepository {

    /**
     * Хранилище для производственного календаря с поиском по ключу
     */
    final private Map<LocalDate, DateInfo> days = new HashMap<>();

    /**
     * Конструктор
     */
    public DefaultDays() {

    }

    /**
     * Добавить в календарь дату
     *
     * @param date - дата
     * @param type - тип {@link DayType}
     */
    public void add(String date, DayType type) {
        add(LocalDate.parse(date), type);
    }

    /**
     * Добавить в календарь дату
     *
     * @param localDate  - дата
     * @param type  - тип {@link DayType}
     * @param title - наименование праздника
     */

    public void add(LocalDate localDate, DayType type, String title) {
        days.put(localDate, new DateInfo(localDate, type, title));
    }

    @Override
    public DateInfo getDateInfo(LocalDate date) {
        if (!days.containsKey(date)) {
            return null;
        }
        return days.get(date);
    }

}
