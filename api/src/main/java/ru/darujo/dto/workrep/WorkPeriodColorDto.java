package ru.darujo.dto.workrep;

import ru.darujo.dto.ColorDto;
import ru.darujo.dto.calendar.DayTypeDto;
import ru.darujo.dto.calendar.WeekWorkDto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class WorkPeriodColorDto extends WeekWorkDto implements Serializable{

    private ColorDto colorDto;
    @SuppressWarnings("unused")
    public WorkPeriodColorDto() {
    }

    public WorkPeriodColorDto(WeekWorkDto weekWorkDto, ColorDto colorDto) {
        this(weekWorkDto.getDayStart(), weekWorkDto.getDayEnd(), weekWorkDto.getTime(), weekWorkDto.getDayTypes(), colorDto);
    }

    public WorkPeriodColorDto(ZonedDateTime dayStart, ZonedDateTime dayEnd, Float time, HashMap<DayTypeDto, Integer> dayTypes, ColorDto colorDto) {
        super(dayStart, dayEnd, time, dayTypes);
        this.colorDto = colorDto;
    }
    @SuppressWarnings("unused")
    @Override
    public ColorDto getColorDto() {
        return colorDto;
    }
}
