package ru.darujo.dto.calendar;

import ru.darujo.assistant.color.ColorRGB;
import ru.darujo.assistant.helper.ColorHelper;
import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.ColorDto;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class WeekWorkDto implements Serializable, Cloneable {
    public WeekWorkDto() {
    }


    private ZonedDateTime dayStart;
    private ZonedDateTime dayEnd;
    private Float time;
    private HashMap<DayTypeDto, Integer> dayTypes;

    public WeekWorkDto(ZonedDateTime dayStart, ZonedDateTime dayEnd, Float time, HashMap<DayTypeDto, Integer> dayTypes) {
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.time = time;
        this.dayTypes = dayTypes;
    }

    public ZonedDateTime getDayStart() {
        return dayStart;
    }

    public ZonedDateTime getDayEnd() {
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
            colorRGB = new ColorRGB(ColorHelper.VACATION_RED, ColorHelper.VACATION_GREEN, ColorHelper.VACATION_BLUE);
            countColor++;
            for (int i = 1; i < count - 1; i++) {
                colorRGB.addColor(ColorHelper.VACATION_COLOR);
                countColor++;
            }

        }
        count = dayTypes.get(DayTypeDto.HOLIDAY);
        if (count != null) {
            if (colorRGB == null) {
                colorRGB = new ColorRGB(ColorHelper.HOLIDAY_RED, ColorHelper.HOLIDAY_GREEN, ColorHelper.HOLIDAY_BLUE);
            } else {
                colorRGB.addColor(ColorHelper.HOLIDAY_COLOR);
            }
            countColor++;
            for (int i = 1; i < count - 1; i++) {
                colorRGB.addColor(ColorHelper.HOLIDAY_COLOR);
                countColor++;
            }
        }
        if (colorRGB != null) {
            long days = Duration.between(dayStart, dayEnd).toDays() + 1;
            for (long i = 1; i < days - countColor; i++) {
                colorRGB.addColor(ColorHelper.WHITE);
            }

        }
        return colorRGB;
    }

    public HashMap<DayTypeDto, Integer> getDayTypes() {
        return dayTypes;
    }



    @SuppressWarnings("unused")
    public String getPeriod() {
        if (getDayStart() == null) {
            return "Итого";
        } else if (getDayEnd() == null || getDayStart().equals(getDayEnd())) {
            return DateHelper.dateToDDMM(getDayStart());
        } else {
            return DateHelper.dateToDDMM(getDayStart()) + " - " + DateHelper.dateToDDMM(getDayEnd());
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
