package ru.darujo.dto.calendar;

import java.io.Serializable;

public class DayDto implements Serializable {
    final private Integer day;
    final private Boolean workDay;
    final private Boolean shotDay;
    final private String comment;

    public DayDto(Integer day, boolean workDay, boolean shotDay, String comment) {
        this.day = day;
        this.workDay = workDay;
        this.shotDay = shotDay;
        this.comment = comment;
    }

    public Integer getDay() {
        return day;
    }

    public boolean isWorkDay() {
        return workDay;
    }

    public boolean isShotDay() {
        return shotDay;
    }

    public String getComment() {
        return comment;
    }
}
