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

    @SuppressWarnings("unused")
    public WeekDto() {
    }

    public WeekDto(DayDto monday, DayDto tuesday, DayDto wednesday, DayDto thursday, DayDto friday, DayDto saturday, DayDto sunday, Integer month) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.month = month;
    }

    @SuppressWarnings("unused")
    public Integer getMonth() {
        return month;
    }

    @SuppressWarnings("unused")
    public void setMonday(DayDto monday) {
        this.monday = monday;
    }

    @SuppressWarnings("unused")
    public void setTuesday(DayDto tuesday) {
        this.tuesday = tuesday;
    }

    @SuppressWarnings("unused")
    public void setWednesday(DayDto wednesday) {
        this.wednesday = wednesday;
    }

    @SuppressWarnings("unused")
    public void setThursday(DayDto thursday) {
        this.thursday = thursday;
    }

    @SuppressWarnings("unused")
    public void setFriday(DayDto friday) {
        this.friday = friday;
    }

    @SuppressWarnings("unused")
    public void setSaturday(DayDto saturday) {
        this.saturday = saturday;
    }

    @SuppressWarnings("unused")
    public void setSunday(DayDto sunday) {
        this.sunday = sunday;
    }

    @SuppressWarnings("unused")
    public DayDto getMonday() {
        return monday;
    }

    @SuppressWarnings("unused")
    public DayDto getTuesday() {
        return tuesday;
    }

    @SuppressWarnings("unused")
    public DayDto getWednesday() {
        return wednesday;
    }

    @SuppressWarnings("unused")
    public DayDto getThursday() {
        return thursday;
    }

    @SuppressWarnings("unused")
    public DayDto getFriday() {
        return friday;
    }

    @SuppressWarnings("unused")
    public DayDto getSaturday() {
        return saturday;
    }

    @SuppressWarnings("unused")
    public DayDto getSunday() {
        return sunday;
    }
}
