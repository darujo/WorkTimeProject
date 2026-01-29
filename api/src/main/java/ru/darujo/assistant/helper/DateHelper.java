package ru.darujo.assistant.helper;

import ru.darujo.exceptions.ResourceNotFoundRunTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static String dateToDDMMYYYY(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    private static final SimpleDateFormat sdfDM = new SimpleDateFormat("dd.MM");


    public static String dateToDDMM(Date date) {
        if (date == null) {
            return null;
        }
        return sdfDM.format(date);
    }

    public static Timestamp DTZToDate(ZonedDateTime dateStr, String text) {
        return DTZToDate(dateStr, text, false);
    }

    public static Timestamp DTZToDate(ZonedDateTime dateStr, String text, boolean checkNull) {
        if (dateStr != null) {
            return dateNoTime(Timestamp.from(dateStr.toInstant()));
        } else if (checkNull) {
            throw new ResourceNotFoundRunTime("Не передан обязательный параметр " + text + " null ");
        }
        return null;
    }

    public static Timestamp dateNoTime(Timestamp dateStr) {
        if (dateStr != null) {

            Calendar c = Calendar.getInstance();
            c.setTime(dateStr);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Timestamp(c.getTimeInMillis());

        } else {
            throw new ResourceNotFoundRunTime("Не задана дата");
        }
    }

    private static final SimpleDateFormat sdfIso = new SimpleDateFormat("yyyy_MM_dd_HH_mm");

    public static String dateToISOStr(Date date) {
        if (date == null) {
            return null;
        }
        return sdfIso.format(date);
    }

    private static DateTimeFormatter dateTimeFormatter;

    public static DateTimeFormatter getDateTimeFormatter() {
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());
        }
        return dateTimeFormatter;
    }

    public static String dateTimeToStr(ZonedDateTime date) {
        if (date == null) {
            return null;
        }

        return date.format(getDateTimeFormatter());
    }

    private static final SimpleDateFormat sdfDT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String dateTimeToStr(Date date) {
        if (date == null) {
            return null;
        }
        return sdfDT.format(date);
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

}
