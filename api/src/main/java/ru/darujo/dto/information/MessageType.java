package ru.darujo.dto.information;

import ru.darujo.type.TypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public enum MessageType implements TypeEnum {
    SYSTEM_INFO("Системные информационые сообщения"),
    ESTIMATION_WORK("Проведена оценка"),
    CHANGE_STAGE_WORK("Смена статуса ЗИ"),
    UPDATE_INFO("Список исправлений в новой версии"),
    AVAIL_WORK_LAST_DAY("Работы отмеченные вами за предыдущий рабочий день. Рассылается по рабочим дням", 11),
    AVAIL_WORK_LAST_WEEK("Работы отмеченные вами за предыдущую неделю. Рассылается на второй рабочий день.", 12),
    AVAIL_WORK_FULL_REPORT("Статус ЗИ.", DayOfWeek.WEDNESDAY, 10),
    WEEK_WORK_REPORT("Факт загрузки за предыдущую неделю.", DayOfWeek.WEDNESDAY, 10),
    ZI_WORK_REPORT("Факт загрузки по ЗИ.", DayOfWeek.WEDNESDAY, 10),
    VACATION_MY_START("Начало вашего отпуска в день перед отпуском", 20),
    VACATION_MY_END("Конец Вашего отпуска в последний день отпуска", 14),
    VACATION_USER_START("Список отпуской ежедневно", 12);

    private final String name;
    private final DayOfWeek dayOfWeek;
    private final Integer hour;
    private final Integer minute;
    private final Integer period;

    MessageType(String name, Integer hour) {
        this(name, null, hour, null, 1);
    }

    MessageType(String name) {
        this(name, null, null, null, null);
    }

    MessageType(String name, DayOfWeek dayOfWeek, Integer hour) {
        this(name, dayOfWeek, hour, null, 7);
    }

    MessageType(String name, DayOfWeek dayOfWeek, Integer hour, Integer minute, Integer periodDay) {
        this.name = name + getDay(dayOfWeek) + getTime(hour, minute);
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
        this.period = periodDay == null ? null : (86400 * periodDay);
    }

    private String getTime(Integer hour, Integer minute) {
        if (hour == null && minute == null) {
            return "";
        }
        return " в " + (hour == null ? "0" : hour) + " часов " + (minute == null ? "" : (minute + " минут"));
    }

    private String getDay(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return "";
        }
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            return " по понедельникам";
        } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            return " по вторникам";
        } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
            return " по средам";
        } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
            return " по четвергам";
        } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            return " по пятницам";
        } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            return " по субботам";
        } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return " по воскресеньям";
        } else return "";
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getPeriod() {
        return period;
    }

    public Long getStartTime() {
        return getStartTime(dayOfWeek, hour, minute);
    }

    private static final Long milliSecondStartDay = getMilliSecondStartDay();

    private static Long getMilliSecondStartDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (System.currentTimeMillis() - c.getTimeInMillis());
    }

    private Long getStartTime(Integer hour, Integer minute) {
        if (minute== null){
            minute = 0;
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы часы");
        }
        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Не верно заданы часы");
        }
        int millisecond = ((hour * 60) + minute) * 60 * 1000;
        long startTime = millisecond - milliSecondStartDay;
        startTime = startTime / 1000;
        if (startTime < 0) {
            startTime = startTime + 24 * 60 * 60;
        }
        return startTime;
    }

    private Long getStartTime(DayOfWeek dayOfWeek, Integer hour, Integer minute) {
        if (dayOfWeek == null) {
            return getStartTime(hour, minute);
        }
        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Не верно заданы часы");
        }
        if (minute== null){
            minute = 0;
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы часы");
        }
        int millisecond = ((hour * 60) + minute) * 60 * 1000;
        long startTime = millisecond - milliSecondStartDay;
        int days = dayOfWeek.getValue() - LocalDate.now().getDayOfWeek().getValue();
        if (days < 0) {
            days = days + 7;
        }
        startTime = startTime / 1000;
        startTime = startTime + days * 86400L;
        if (startTime < 0) {
            startTime = startTime + 7 * 24 * 60 * 60;
        }
        return startTime;
    }
}
