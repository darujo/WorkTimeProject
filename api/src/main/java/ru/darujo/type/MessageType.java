package ru.darujo.type;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public enum MessageType implements TypeEnum {
    SYSTEM_INFO("Системные информационные сообщения"),
    ESTIMATION_WORK("Проведена оценка"),
    //todo Переименовать
    CHANGE_STAGE_WORK("Изменение ЗИ (статус или релиз) "),
    UPDATE_INFO("Список исправлений в новой версии"),
    AVAIL_WORK_LAST_DAY("Работы отмеченные вами за предыдущий рабочий день. Рассылается по рабочим дням", 11),
    AVAIL_WORK_LAST_WEEK("Работы отмеченные вами за последние 7 дней. Рассылается на второй рабочий день.", 12),
    AVAIL_WORK_FULL_REPORT("Статус ЗИ", ReportType.ZI_STATUS, false, DayOfWeek.TUESDAY, 20),
    AVAIL_WORK_FULL_REPORT_PROJECT("Статус ЗИ по проектам.", ReportType.ZI_STATUS_PROJECT, true, DayOfWeek.TUESDAY, 20, 10),

    WEEK_WORK_REPORT("Факт загрузки за предыдущую неделю.", ReportType.USER_WORK, false, DayOfWeek.TUESDAY, 20),
    ZI_WORK_REPORT("Факт загрузки по ЗИ.", ReportType.ZI_WORK, false, DayOfWeek.TUESDAY, 20),
    ZI_WORK_REPORT_PROJECT("Факт загрузки по ЗИ по проектам.", ReportType.ZI_WORK_PROJECT, true, DayOfWeek.TUESDAY, 20, 10),

    VACATION_MY_START("Начало вашего отпуска в день перед отпуском", 20),
    VACATION_MY_END("Конец Вашего отпуска в последний день отпуска", 14),
    VACATION_USER_START("Список отпусков ежедневно", 12),
    EDIT_WORK_REQUEST("Добавлен/изменен запрос на согласование ТЗ"),
    EDIT_WORK_RESPONSE("Добавлено/изменено согласование ТЗ");

    private final String name;
    private final DayOfWeek dayOfWeek;
    private final Integer hour;
    private final Integer minute;
    private final Integer period;
    private final boolean project;
    private List<ReportType> reportTypeList;

    // Уведомления без периода
    MessageType(String name) {
        this(name, false, null, null, null, null);
    }

    // Ежедневные
    MessageType(String name, Integer hour) {
        this(name, false, null, hour, null, 1);
    }

    // Еженедельные
    MessageType(String name, ReportType reportType, Boolean project, DayOfWeek dayOfWeek, Integer hour) {
        this(name, reportType, project, dayOfWeek, hour, null);
    }

    MessageType(String name, ReportType reportType, Boolean project, DayOfWeek dayOfWeek, Integer hour, Integer minute) {
        this(name, project, dayOfWeek, hour, minute, 7);

        if (reportType != null) {
            reportTypeList = new ArrayList<>();
            reportTypeList.add(reportType);
        }


    }


    MessageType(String name, Boolean project, DayOfWeek dayOfWeek, Integer hour, Integer minute, Integer periodDay) {
        this.name = name + getDay(dayOfWeek) + getTime(hour, minute);
        this.project = project;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
        this.period = periodDay == null ? null : (86400 * periodDay);
//        this.reportTypeList = reportTypeList;
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
        if (minute == null) {
            minute = 0;
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы минуты");
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
        if (minute == null) {
            minute = 0;
        }
        if (minute < 0 || minute > 59) {
            throw new RuntimeException("Не верно заданы минуты");
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

    public boolean isProject() {
        return project;
    }

    public List<ReportType> getReportTypeList() {
        return reportTypeList;
    }

}
