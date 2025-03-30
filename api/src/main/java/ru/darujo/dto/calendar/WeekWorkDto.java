package ru.darujo.dto.calendar;

import ru.darujo.dto.WorkTimeDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class WeekWorkDto implements Serializable {
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

}
