package ru.darujo.utils.calendar;

import ru.darujo.utils.calendar.days.DefaultDays;
import ru.darujo.utils.calendar.days.ProductionCalendarDaysInterface;
import ru.darujo.utils.calendar.days.RU_Days;
import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ProductionCalendar {

    ProductionCalendarDaysInterface productionCalendarDaysInterface;

    /**
     * @param productionCalendarDaysInterface ProductionCalendarDaysInterface. Наследованный от {@link DefaultDays} объект
     */
    public ProductionCalendar(ProductionCalendarDaysInterface productionCalendarDaysInterface) {
        productionCalendarDaysInterface.init();
        this.productionCalendarDaysInterface = productionCalendarDaysInterface;
    }

    /**
     * Конструктор по умолчанию. Без локального производственного календаря.
     */
    public ProductionCalendar() {
        this(new RU_Days());
    }



    /**
     * Возвращает true, если date НЕ выпадает на выходные (Рабочая неделя)
     *
     * @param date LocalDate - Дата для проверки
     * @return boolean
     */
    public boolean isWorkWeekNotCalendar(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * Возвращает true, если date отмечен в производственном календаре
     * как Сокращенный рабочий день (DayType.SHORTDAY)
     *
     * @param date LocalDate - Дата для проверки
     * @return boolean
     */
    public boolean isShortDay(LocalDate date) {
        return productionCalendarDaysInterface.getType(date) == DayType.SHORTDAY;
    }

    /**
     * Возвращает true, если date отмечен в производственном календаре
     * как Рабочий день (DayType.WORKDAY) или date на рабочей неделе
     * и НЕ отмечен как выходной (DayType.HOLIDAY)
     *
     * @param date LocalDate - Дата для проверки
     * @return boolean
     */
    public boolean isWorkDay(LocalDate date) {

        DayType dayType = productionCalendarDaysInterface.getType(date);
        if (dayType != null) {
            if (dayType.equals(DayType.WORKDAY) || dayType.equals(DayType.SHORTDAY))
                return true;
            if (dayType.equals(DayType.HOLIDAY) || dayType.equals(DayType.WEEK_END))
                return false;
        }
        return isWorkWeekNotCalendar(date);
    }

    /**
     * Возвращает true, если date отмечен в производственном календаре
     * как Выходной день (DayType.HOLIDAY) или date выходной день (Суббота или Воскресенье)
     * и НЕ отмечен как Рабочий день (DayType.WORKDAY) или Сокращенный рабочий день (DayType.SHORTDAY)
     *
     * @param date LocalDate - Дата для проверки
     * @return boolean
     */
    public boolean isWeekEnd(LocalDate date) {

        DayType dayType = productionCalendarDaysInterface.getType(date);
        if (dayType != null) {
            if (dayType.equals(DayType.HOLIDAY) || dayType.equals(DayType.WEEK_END))
                return true;
            if (dayType.equals(DayType.WORKDAY) || dayType.equals(DayType.SHORTDAY))
                return false;
        }
        return productionCalendarDaysInterface.isWeekEnd(date);
    }

    /**
     * Возвращает дату через dayInterval календарных дней от date
     *
     * @param date        LocalDate - дата начала отсчета
     * @param dayInterval int - интервал в календарных днях
     * @return LocalDate
     */
    public LocalDate getDateAfterInterval(LocalDate date, int dayInterval) {
        return date.plusDays(dayInterval);
    }

    /**
     * Возвращает дату через dayInterval календарных дней от date
     *
     * @param date            LocalDate - дата начала отсчета
     * @param dayInterval     int - интервал в календарных днях
     * @param includeFirstDay boolean - включить в расчет текущий день
     * @return LocalDate
     */
    public LocalDate getDateAfterInterval(LocalDate date, int dayInterval, boolean includeFirstDay) {
        if (includeFirstDay && dayInterval > 0) dayInterval--;
        return date.plusDays(dayInterval);
    }

    /**
     * Возвращает ближайший рабочей день через dayInterval календарных дней от date
     *
     * @param date        LocalDate - дата начала отсчета
     * @param dayInterval int - интервал в РАБОЧИХ днях
     * @return LocalDate
     */
    public LocalDate getWorkDateAfterInterval(LocalDate date, int dayInterval) {
        LocalDate result = date.plusDays(dayInterval);
        while (isWeekEnd(result))
            result = result.plusDays(1);
        return result;
    }

    /**
     * Возвращает ближайший рабочей день через dayInterval календарных дней от date
     *
     * @param date            LocalDate - дата начала отсчета
     * @param dayInterval     int - интервал в РАБОЧИХ днях
     * @param includeFirstDay boolean - включить в расчет текущий день
     * @return LocalDate
     */
    public LocalDate getWorkDateAfterInterval(LocalDate date, int dayInterval, boolean includeFirstDay) {
        if (includeFirstDay && dayInterval > 0) dayInterval--;
        LocalDate result = date.plusDays(dayInterval);
        while (isWeekEnd(result))
            result = result.plusDays(1);
        return result;
    }

    /**
     * Возвращает ближайший рабочий день через dayInterval РАБОЧИХ дней от date
     *
     * @param date            LocalDate - дата начала отсчета
     * @param workDayInterval int - интервал в РАБОЧИХ днях
     * @return LocalDate
     */
    public LocalDate getDateAfterWorkDaysInterval(LocalDate date, int workDayInterval) {
        for (int i = 0; i < workDayInterval; i++) {
            date = getWorkDateAfterInterval(date, 1);
        }
        return date;
    }

    /**
     * Возвращает ближайший рабочий день через dayInterval РАБОЧИХ дней от date
     *
     * @param date            LocalDate - дата начала отсчета
     * @param workDayInterval int - интервал в РАБОЧИХ днях
     * @param includeFirstDay boolean - включить в расчет текущий день
     * @return LocalDate
     */
    public LocalDate getDateAfterWorkDaysInterval(LocalDate date, int workDayInterval, boolean includeFirstDay) {
        if (includeFirstDay && workDayInterval > 0) workDayInterval--;
        for (int i = 0; i < workDayInterval; i++) {
            date = getWorkDateAfterInterval(date, 1);
        }
        return date;
    }

    /**
     * Возвращает информацию об указанной дате (дата, тип, наименование праздника)
     *
     * @param date LocalDate - дата проверки
     * @return DateInfo - информация об указанной дате {@link DateInfo}
     */
    public DateInfo getDateInfo(LocalDate date) {
        DateInfo dateInfo = productionCalendarDaysInterface.getDateInfo(date);
        if (dateInfo != null)
            return dateInfo;
        else if (isWorkDay(date))
            return new DateInfo(date, DayType.WORKDAY);
        else if (isWeekEnd(date))
            return new DateInfo(date, DayType.WEEK_END);
        else
            return null;
    }

    public boolean isHoliday(LocalDate date) {
        return DayType.HOLIDAY.equals(productionCalendarDaysInterface.getType(date));
    }

    public Integer iDaysNotHoliday(LocalDate dateStart, LocalDate dateEnd) {
        int days = 0;
        while (!dateStart.isAfter(dateEnd)) {
            if (!isHoliday(dateStart)) {
                days++;
            }
            dateStart = dateStart.plusDays(1);
        }
        return days;
    }

    public LocalDate getDateEndNotHoliday(LocalDate dateStart, int daysAdd) {
        int days = 1;
        while (days < daysAdd || isHoliday(dateStart)) {
            if (!isHoliday(dateStart)) {
                days++;
            }
            dateStart = dateStart.plusDays(1);
        }
        return dateStart;
    }

    public boolean existWorkDay(LocalDate dateStart, LocalDate dateEnd) {
        while (!dateStart.isAfter(dateEnd)) {
            if (isWorkDay(dateStart)) {
                return true;
            }
            dateStart = dateStart.plusDays(1);
        }
        return false;
    }
}

