package ru.darujo.assistant.helper;

import ru.darujo.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class DataHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    public static String dateToDDMMYYYY(Date date){
        if (date == null){
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

            Calendar c = Calendar.getInstance();
            c.setTime(Timestamp.from(dateStr.toInstant()));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Timestamp(c.getTimeInMillis());

        } else if (checkNull) {
            throw new ResourceNotFoundException("Не передан обязательный параметр " + text + " null ");
        }
        return null;
    }
}
