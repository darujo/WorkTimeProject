package ru.darujo.dto.calendar;

import java.io.Serializable;

public class WeekDto implements Serializable {
    private DayDto monday;
    private DayDto tuesday;
    private DayDto wednesday;
    private DayDto thursday;
    private DayDto friday;
    private DayDto saturday;
    private DayDto sunday;
    private Integer month;

    public WeekDto(DayDto monday, DayDto tuesday, DayDto wednesday, DayDto thursday, DayDto friday, DayDto saturday, DayDto sunday,Integer month) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.month = month;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonday(DayDto monday) {
        this.monday = monday;
    }

    public void setTuesday(DayDto tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(DayDto wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(DayDto thursday) {
        this.thursday = thursday;
    }

    public void setFriday(DayDto friday) {
        this.friday = friday;
    }

    public void setSaturday(DayDto saturday) {
        this.saturday = saturday;
    }

    public void setSunday(DayDto sunday) {
        this.sunday = sunday;
    }

    public DayDto getMonday() {
        return monday;
    }

    public DayDto getTuesday() {
        return tuesday;
    }

    public DayDto getWednesday() {
        return wednesday;
    }

    public DayDto getThursday() {
        return thursday;
    }

    public DayDto getFriday() {
        return friday;
    }

    public DayDto getSaturday() {
        return saturday;
    }

    public DayDto getSunday() {
        return sunday;
    }
}
