package ru.darujo.dto.workrep;

import ru.darujo.dto.ColorDto;
import ru.darujo.dto.calendar.WeekWorkDto;

import java.io.Serializable;
import java.sql.Timestamp;

public class WorkPeriodColorDto extends WeekWorkDto implements Serializable{

    private ColorDto colorDto;
    @SuppressWarnings("unused")
    public WorkPeriodColorDto() {
    }

    public WorkPeriodColorDto(WeekWorkDto weekWorkDto, ColorDto colorDto) {
        this(weekWorkDto.getDayStart(), weekWorkDto.getDayEnd(), weekWorkDto.getTime(), colorDto);
    }

    public WorkPeriodColorDto(Timestamp dayStart, Timestamp dayEnd, Float time, ColorDto colorDto) {
        super(dayStart, dayEnd, time);
        this.colorDto = colorDto;
    }
    @SuppressWarnings("unused")
    public ColorDto getColorDto() {
        return colorDto;
    }
}
