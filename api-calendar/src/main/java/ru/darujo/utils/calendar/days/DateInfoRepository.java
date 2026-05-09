package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.function.Consumer;

public abstract class DateInfoRepository {
    public void add(String date, DayType type) {
        add(LocalDate.parse(date), type);
    }

    public void add(LocalDate date, DayType dayType) {
        add(date, dayType, null);
    }

    public void add(String date, DayType type, String title) {
        add(LocalDate.parse(date), type, title);
    }

    public abstract void add(LocalDate date, DayType dayType, String title);

    public abstract DateInfo getDateInfo(LocalDate date);

    /**
     * Возвращает true, если date выпадает на выходные (Суббота или Воскресенье)
     *
     * @param date LocalDate - Дата для проверки
     * @return boolean
     */
    public boolean isWeekEnd(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    protected Consumer<Integer> addYear;

    public void setAddYear(Consumer<Integer> addYear) {
        this.addYear = addYear;
    }
}
