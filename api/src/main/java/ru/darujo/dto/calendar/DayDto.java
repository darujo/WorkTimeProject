package ru.darujo.dto.calendar;

import java.io.Serializable;

public class DayDto implements Serializable {
    final private Integer day;
    final private Boolean weekend;
    final private Boolean shotDay;
    final private String comment;

    public DayDto(Integer day, boolean weekend, boolean shotDay, String comment) {
        this.day = day;
        this.weekend = weekend;
        this.shotDay = shotDay;
        this.comment = comment;
    }

    public Integer getDay() {
        return day;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public boolean isShotDay() {
        return shotDay;
    }

    public String getComment() {
        return comment;
    }
}
