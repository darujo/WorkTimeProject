package ru.darujo.dto.calendar;

import ru.darujo.assistant.color.ColorRGB;
import ru.darujo.dto.ColorDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class WeekWorkDto implements Serializable, Cloneable {
    public WeekWorkDto() {
    }

    private Timestamp dayStart;
    private Timestamp dayEnd;
    private Float time;
    private HashSet<DayTypeDto> dayTypes;


    public WeekWorkDto(Timestamp dayStart, Timestamp dayEnd, Float time, HashSet<DayTypeDto> dayTypes) {
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.time = time;
        this.dayTypes = dayTypes;
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

    public void addDayType(DayTypeDto dayType) {
        dayTypes.add(dayType);
    }


    @SuppressWarnings("unused")
    public ColorDto getColorDto() {
        if(dayTypes == null){
            return null;
        }
        if (dayTypes.contains(DayTypeDto.VACATION)) {
            if (dayTypes.contains(DayTypeDto.HOLIDAY)) {
                return new ColorRGB(178, 34, 34);
            } else {
                return new ColorRGB(240, 128, 128);
            }
        }
        return null;
    }

    public HashSet<DayTypeDto> getDayTypes() {
        return dayTypes;
    }

    final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");


    protected String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    @SuppressWarnings("unused")
    public String getPeriod() {
        if (getDayStart() == null) {
            return "Итого";
        } else if (getDayEnd() == null || getDayStart().equals(getDayEnd())) {
            return dateToText(getDayStart());
        } else {
            return dateToText(getDayStart()) + " - " + dateToText(getDayEnd());
        }

    }

    @Override
    public Object clone() {
        try {
            WeekWorkDto weekWorkDto = (WeekWorkDto) super.clone();
            weekWorkDto.dayTypes = (HashSet<DayTypeDto>)  weekWorkDto.dayTypes.clone();
            return weekWorkDto;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
