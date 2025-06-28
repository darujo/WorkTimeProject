package ru.darujo.dto.workrep;

import ru.darujo.dto.ColorDto;
import ru.darujo.dto.calendar.WeekWorkDto;
import ru.darujo.dto.calendar.DayTypeDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class WorkPeriodColorDto extends WeekWorkDto implements Serializable{

    private ColorDto colorDto;
    @SuppressWarnings("unused")
    public WorkPeriodColorDto() {
    }

    public WorkPeriodColorDto(WeekWorkDto weekWorkDto, ColorDto colorDto) {
        this(weekWorkDto.getDayStart(), weekWorkDto.getDayEnd(), weekWorkDto.getTime(), weekWorkDto.getDayTypes(), colorDto);
    }

    public WorkPeriodColorDto(Timestamp dayStart, Timestamp dayEnd, Float time, HashMap<DayTypeDto,Integer> dayTypes, ColorDto colorDto) {
        super(dayStart, dayEnd, time, dayTypes);
        this.colorDto = colorDto;
    }
    @SuppressWarnings("unused")
    @Override
    public ColorDto getColorDto() {
        return colorDto;
    }
}
