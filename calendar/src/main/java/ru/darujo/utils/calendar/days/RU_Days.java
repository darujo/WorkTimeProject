package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DayType;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Производственный календарь для РФ 2013-2019гг.
 */
public class RU_Days extends DefaultDays {

    static private final String NEW_YEAR = "Новогодние каникулы (в ред. Федерального закона от 23.04.2012  35-ФЗ)";
    static private final String CHRISTMAS = "Рождество Христово";
    static private final String MENDAY = "День защитника Отечества";
    static private final String WOMENDAY = "Международный женский день";
    static private final String MAYDAY = "Праздник Весны и Труда";
    static private final String VICTORYDAY = "День Победы";
    static private final String RUSSIADAY = "День России";
    static private final String PEOPLEDAY = "День народного единства";


    @Override
    public void init() {
        for (int year = 2025; year < 2050; year++) {
            add(year + "-01-01", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-02", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-03", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-04", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-05", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-06", DayType.HOLIDAY, NEW_YEAR);
            add(year + "-01-07", DayType.HOLIDAY, CHRISTMAS);
            add(year + "-01-08", DayType.HOLIDAY, NEW_YEAR);
            setShortDay(year,2,22);
            add(year + "-02-23", DayType.HOLIDAY, MENDAY);
            setShortDay(year ,3,7);
            add(year + "-03-08", DayType.HOLIDAY, WOMENDAY);
            setShortDay(year, 4,30);
            add(year + "-05-01", DayType.HOLIDAY, MAYDAY);
            setShortDay(year, 5,8);
            add(year + "-05-09", DayType.HOLIDAY, VICTORYDAY);
            setShortDay(year,6,11);
            add(year + "-06-12", DayType.HOLIDAY, RUSSIADAY);
            setShortDay(year, 11,3);
            add(year + "-11-04", DayType.HOLIDAY, PEOPLEDAY);
            setShortDay(year,12,31);
        }
        add("2025-05-02", DayType.WEEK_END,"с субботы 4 января на пятницу 2 мая");
        add("2025-12-31", DayType.WEEK_END,"с воскресенья 5 января на среду 31 декабря");
        add("2025-05-08", DayType.WEEK_END,"с воскресенья 23 февраля на четверг 8 мая");
        add("2025-06-13", DayType.WEEK_END,"с субботы 8 марта на пятницу 13 июня;");
        add("2025-11-01", DayType.WORKDAY,"с субботы 1 ноября на понедельник 3 ноября");
        add("2025-11-03", DayType.WEEK_END,"с субботы 1 ноября на понедельник 3 ноября");

        add("2026-01-09", DayType.WEEK_END,"с субботы 3 января на пятницу 9 января");
        add("2026-12-31", DayType.WEEK_END,"с воскресенья 4 января на четверг 31 декабря");



    }
    public boolean isDayWork(LocalDate date){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    private void setShortDay(int year, int month, int day ){
        LocalDate date = LocalDate.of(year,month,day);
        if (isDayWork(date)){
            String monthStr;
            if(month< 10){
                monthStr = "0" + month;
            }
            else {
                monthStr = "" + month;
            }
            String dayStr;
            if(day< 10){
                dayStr = "0" + day;
            }
            else {
                dayStr = "" + day;
            }
            add(year + "-" + monthStr + "-" + dayStr, DayType.SHORTDAY);
        }

    }
}
