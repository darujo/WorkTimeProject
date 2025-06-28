package ru.darujo.dto.calendar;

import ru.darujo.assistant.color.ColorRGB;
import ru.darujo.dto.ColorDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

public class WeekWorkDto implements Serializable, Cloneable {
    public WeekWorkDto() {
    }

    private final Integer HOLIDAY_RED = 188; //255; //178;
    private final Integer HOLIDAY_GREEN = 143; //69; //34;
    private final Integer HOLIDAY_BLUE = 143; //0; //34;
    private final ColorRGB HOLIDAY_COLOR = new ColorRGB(HOLIDAY_RED, HOLIDAY_GREEN, HOLIDAY_BLUE);
    private final Integer VACATION_RED = 165; // 240
    private final Integer VACATION_GREEN = 42; // 128
    private final Integer VACATION_BLUE = 42;  // 128
    private final ColorRGB VACATION_COLOR = new ColorRGB(VACATION_RED, VACATION_GREEN, VACATION_BLUE);
    private Timestamp dayStart;
    private Timestamp dayEnd;
    private Float time;
    private HashMap<DayTypeDto, Integer> dayTypes;


    public WeekWorkDto(Timestamp dayStart, Timestamp dayEnd, Float time, HashMap<DayTypeDto, Integer> dayTypes) {
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
        Integer day = dayTypes.get(dayType);
        if (day == null) {
            dayTypes.put(dayType, 1);
        } else {
            dayTypes.put(dayType, ++day);
        }
    }

    public void deleteDayType(DayTypeDto dayType) {
        dayTypes.remove(dayType);
    }


    @SuppressWarnings("unused")
    public ColorDto getColorDto() {
        if (dayTypes == null) {
            return null;
        }
        int countColor = 0;
        ColorRGB colorRGB = null;
        Integer count = dayTypes.get(DayTypeDto.VACATION);
        if (count != null) {
            colorRGB = new ColorRGB(VACATION_RED, VACATION_GREEN, VACATION_BLUE);
            countColor++;
            for (int i = 1; i < count - 1; i++) {
                colorRGB.addColor(VACATION_COLOR);
                countColor++;
            }

        }
        count = dayTypes.get(DayTypeDto.HOLIDAY);
        if (count != null) {
            if (colorRGB == null) {
                colorRGB = new ColorRGB(HOLIDAY_RED, HOLIDAY_GREEN, HOLIDAY_BLUE);
            } else {
                colorRGB.addColor(HOLIDAY_COLOR);
            }
            countColor++;
            for (int i = 1; i < count - 1; i++) {
                colorRGB.addColor(HOLIDAY_COLOR);
                countColor++;
            }
        }
        if (colorRGB != null) {
            long days = Duration.between(dayStart.toLocalDateTime(), dayEnd.toLocalDateTime()).toDays() + 1;
            ColorRGB withe = new ColorRGB(255, 255, 255);
            for (long i = 1; i < days - countColor; i++) {
                colorRGB.addColor(withe);
            }

        }
        return colorRGB;
    }

    public HashMap<DayTypeDto, Integer> getDayTypes() {
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
            weekWorkDto.dayTypes = new HashMap<>(weekWorkDto.dayTypes);

            return weekWorkDto;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
