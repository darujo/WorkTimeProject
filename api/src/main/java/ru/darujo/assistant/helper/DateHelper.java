package ru.darujo.assistant.helper;

import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class DateHelper {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter dateLittleFormatter = DateTimeFormatter.ofPattern("dd.MM").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter dateIsoFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm").withZone(ZoneId.systemDefault());

    public static ZonedDateTime getZDT(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
    }

    public static ZonedDateTime getZDT(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
    }

    public static LocalDate zDTToLD(ZonedDateTime zonedDateTime) {
        return zDTToLD(zonedDateTime, null);
    }

    public static LocalDate zDTToLD(ZonedDateTime zonedDateTime, String text) {
        if (text != null) {
            checkNull(zonedDateTime, text);
        }
        if (zonedDateTime == null) {
            return null;
        }
        return LocalDate.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime zDTToLDT(ZonedDateTime zonedDateTime) {
        return zDTToLDT(zonedDateTime, null);
    }

    public static LocalDateTime zDTToLDT(ZonedDateTime zonedDateTime, String text) {
        if (text != null) {
            checkNull(zonedDateTime, text);
        }
        if (zonedDateTime == null) {
            return null;
        }
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneId.systemDefault());
    }

    public static void checkNull(Object localDate, String text) {
        if (localDate == null) {
            throw new ResourceNotFoundRunTime("Не передан обязательный параметр " + text + " null ");
        }
    }

    public static String dateToISOStr(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(dateIsoFormatter);
    }

    public static String dateTimeToStr(ZonedDateTime zonedDateTime) {
        return dateTimeToStr(zDTToLDT(zonedDateTime));
    }


    public static String dateTimeToStr(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.format(dateTimeFormatter);
    }

    public static List<Long> convertListToLong(List<String> list) {
        if (list == null) {
            return null;
        }
        return list
                .stream()
                .filter(s -> s != null && !s.equals("null") && !s.equals("undefined"))
                .map(Long::parseLong).toList();
    }

    public static List<String> convertListNotNull(List<String> list) {
        if (list == null
                || list.contains(null)
                || list.contains("null")
                || list.contains("undefined")) {
            return null;
        }

        return list;
    }

    public static String dateToDDMMYYYY(ZonedDateTime zonedDateTime) {
        return dateToDDMMYYYY(zDTToLD(zonedDateTime));
    }

    public static String dateToDDMMYYYY(LocalDate zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }

        return zonedDateTime.format(dateFormatter);
    }

    public static String dateToDDMM(ZonedDateTime zonedDateTime) {
        return dateToDDMM(zDTToLD(zonedDateTime));
    }


    public static String dateToDDMM(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(dateLittleFormatter);
    }

    private static final Long milliSecondStartDay = getMilliSecondStartDay();

    public static Long getStartTime(Integer hour, Integer minute) {
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

    private static Long getMilliSecondStartDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (System.currentTimeMillis() - c.getTimeInMillis());
    }

    public static Long getStartTime(DayOfWeek dayOfWeek, Integer hour, Integer minute) {
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
}
