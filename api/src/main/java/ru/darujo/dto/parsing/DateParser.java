package ru.darujo.dto.parsing;

import ru.darujo.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class DateParser {
    protected Timestamp stringToDate(ZonedDateTime dateStr, String text) {
        return stringToDate(dateStr, text, false);
    }

    protected Timestamp stringToDate(ZonedDateTime dateStr, String text, boolean checkNull) {
        if (dateStr != null) {

            Calendar c = Calendar.getInstance();
            c.setTime(Timestamp.from(dateStr.toInstant()));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Timestamp(c.getTimeInMillis());

        } else if (checkNull) {
            throw new ResourceNotFoundException("Не не передан обязательный параметр " + text + " null ");
        }
        return null;
    }
}
