package ru.darujo.dto.calendar;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeekWorkDto implements Serializable,Cloneable {
    public WeekWorkDto() {
    }

    private Timestamp dayStart;
    private Timestamp dayEnd;
    private Float time;


    public WeekWorkDto(Timestamp dayStart, Timestamp dayEnd, Float time) {
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.time = time;
    }

    public Timestamp getDayStart() {
        return dayStart;
    }

    public Timestamp getDayEnd() {
        return dayEnd;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");


    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }
    public String getPeriod() {
        if(getDayStart() == null){
            return "Итого";
        }
        else if (getDayEnd() == null || getDayStart().equals(getDayEnd())) {
            return dateToText(getDayStart());
        } else {
            return dateToText(getDayStart()) + " - " + dateToText(getDayEnd());
        }

    }
}
