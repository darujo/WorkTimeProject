package ru.darujo.converter;

import ru.darujo.model.DayInfo;
import ru.darujo.utils.calendar.structure.DateInfo;
import ru.darujo.utils.calendar.structure.DayType;

public class DayInfoConverter {

    public static DayInfo getDayInfo(DateInfo dayInfoDto) {
        return new DayInfo(null, dayInfoDto.getDate(), dayInfoDto.getType().toString(), dayInfoDto.getTitle());
    }

    public static DateInfo getDateInfo(DayInfo dayInfo) {
        return new DateInfo(dayInfo.getDate(), DayType.valueOf(dayInfo.getType()), dayInfo.getTitle());
    }
}
