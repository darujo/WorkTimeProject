package ru.darujo.dto.workrep;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.dto.calendar.WeekWorkDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WorkPeriodDto extends WeekWorkDto implements Serializable {
    public WorkPeriodDto() {
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");


    private String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    public WorkPeriodDto(WeekWorkDto weekWorkDto, List<WorkTimeDto> list) {
        this(weekWorkDto.getDayStart(), weekWorkDto.getDayEnd(), weekWorkDto.getTime(), list);
    }

    public WorkPeriodDto(Timestamp dayStart, Timestamp dayEnd, Float time, List<WorkTimeDto> list) {
        super(dayStart, dayEnd, time);
        this.list = list;
    }

    private List<WorkTimeDto> list;

    public List<WorkTimeDto> getList() {
        return list;
    }

    public String getPeriod() {
        if(getDayStart() == null){
            return "Итого";
        }
        else if (getDayEnd() == null || getDayStart().equals(getDayEnd())) {
            return dateToText(getDayStart());
        } else {
            return dateToText(getDayStart()) + " - " + dateToText(getDayEnd());
        }

    }

}
