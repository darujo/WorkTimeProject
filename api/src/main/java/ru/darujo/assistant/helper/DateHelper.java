package ru.darujo.assistant.helper;

import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
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
}
