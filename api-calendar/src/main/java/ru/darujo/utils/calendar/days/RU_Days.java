package ru.darujo.utils.calendar.days;

import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

import java.time.LocalDate;
import java.time.Year;

/**
 * Производственный календарь для РФ 2025-2027гг.
 */
public class RU_Days implements ProductionCalendarDaysInterface {

    static private final String NEW_YEAR = "Новогодние каникулы (в ред. Федерального закона от 23.04.2012  35-ФЗ)";
    static private final String CHRISTMAS = "Рождество Христово";
    static private final String MEN_DAY = "День защитника Отечества";
    static private final String WOMEN_DAY = "Международный женский день";
    static private final String MAYDAY = "Праздник Весны и Труда";
    static private final String VICTORY_DAY = "День Победы";
    static private final String RUSSIA_DAY = "День России";
    static private final String PEOPLE_DAY = "День народного единства";

    private final DateInfoRepository dateInfoRepository;

    public RU_Days() {
        this(new DefaultDays());
    }

    public RU_Days(DateInfoRepository dateInfoRepository) {
        this.dateInfoRepository = dateInfoRepository;
        this.dateInfoRepository.setAddYear(this::initYear);
    }

    @Override
    public void init() {
        for (int year = 2025; year < Year.now().getValue() + 1; year++) {
            initYear(year);
        }
        dateInfoRepository.add("2025-05-02", DayType.WEEK_END, "с субботы 4 января на пятницу 2 мая");
        dateInfoRepository.add("2025-12-31", DayType.WEEK_END, "с воскресенья 5 января на среду 31 декабря");
        dateInfoRepository.add("2025-05-08", DayType.WEEK_END, "с воскресенья 23 февраля на четверг 8 мая");
        dateInfoRepository.add("2025-06-13", DayType.WEEK_END, "с субботы 8 марта на пятницу 13 июня;");
        dateInfoRepository.add("2025-11-01", DayType.SHORTDAY, "с субботы 1 ноября на понедельник 3 ноября");
        dateInfoRepository.add("2025-11-03", DayType.WEEK_END, "с субботы 1 ноября на понедельник 3 ноября");

        dateInfoRepository.add("2026-01-09", DayType.WEEK_END, "с субботы 3 января на пятницу 9 января");
        dateInfoRepository.add("2026-03-09", DayType.WEEK_END, "В связи с Международный женским денём");
        dateInfoRepository.add("2026-05-11", DayType.WEEK_END, "В связи с Днём победы");
        dateInfoRepository.add("2026-12-31", DayType.WEEK_END, "с воскресенья 4 января на четверг 31 декабря");


        dateInfoRepository.add("2027-11-05", DayType.WEEK_END, "с субботы 2 января на пятницу 5 ноября");
        dateInfoRepository.add("2027-12-31", DayType.WEEK_END, "с воскресенья 3 января на пятницу 31 декабря");
        dateInfoRepository.add("2027-05-10", DayType.WEEK_END, "В связи с Днём победы");
        dateInfoRepository.add("2027-02-20", DayType.SHORTDAY, "с субботы 20 февраля на понедельник 22 февраля");
        dateInfoRepository.add("2027-02-22", DayType.WEEK_END, "с субботы 20 февраля на понедельник 22 февраля");

    }

    private void initYear(int year) {
        dateInfoRepository.add(year + "-01-01", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-02", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-03", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-04", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-05", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-06", DayType.HOLIDAY, NEW_YEAR);
        dateInfoRepository.add(year + "-01-07", DayType.HOLIDAY, CHRISTMAS);
        dateInfoRepository.add(year + "-01-08", DayType.HOLIDAY, NEW_YEAR);
        setShortDay(year, 2, 22);
        dateInfoRepository.add(year + "-02-23", DayType.HOLIDAY, MEN_DAY);
        setShortDay(year, 3, 7);
        dateInfoRepository.add(year + "-03-08", DayType.HOLIDAY, WOMEN_DAY);
        setShortDay(year, 4, 30);
        dateInfoRepository.add(year + "-05-01", DayType.HOLIDAY, MAYDAY);
        setShortDay(year, 5, 8);
        dateInfoRepository.add(year + "-05-09", DayType.HOLIDAY, VICTORY_DAY);
        setShortDay(year, 6, 11);
        dateInfoRepository.add(year + "-06-12", DayType.HOLIDAY, RUSSIA_DAY);
        setShortDay(year, 11, 3);
        dateInfoRepository.add(year + "-11-04", DayType.HOLIDAY, PEOPLE_DAY);
        setShortDay(year, 12, 31);
    }


    public boolean isDayWork(LocalDate date) {
        return !dateInfoRepository.isWeekEnd(date);
    }

    private void setShortDay(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        if (isDayWork(date)) {
            String monthStr;
            if (month < 10) {
                monthStr = "0" + month;
            } else {
                monthStr = "" + month;
            }
            String dayStr;
            if (day < 10) {
                dayStr = "0" + day;
            } else {
                dayStr = "" + day;
            }
            dateInfoRepository.add(year + "-" + monthStr + "-" + dayStr, DayType.SHORTDAY);
        }

    }

    public DateInfo getDateInfo(LocalDate date) {
        return dateInfoRepository.getDateInfo(date);
    }

    public boolean isWeekEnd(LocalDate date) {
        return dateInfoRepository.isWeekEnd(date);
    }
}
