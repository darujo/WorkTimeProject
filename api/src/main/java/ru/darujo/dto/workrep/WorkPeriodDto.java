package ru.darujo.dto.workrep;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.dto.calendar.WeekWorkDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class WorkPeriodDto extends WeekWorkDto implements Serializable {

    private Boolean isShotVacation;
    private Boolean isAllVacation;

    @SuppressWarnings("unused")
    public WorkPeriodDto() {
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

    @SuppressWarnings("unused")
    public Boolean getShotVacation() {
        return isShotVacation;
    }

    @SuppressWarnings("unused")
    public Boolean getAllVacation() {
        return isAllVacation;
    }

    public void setShotVacation(Boolean shotVacation) {
        isShotVacation = shotVacation;
    }

    public void setAllVacation(Boolean allVacation) {
        isAllVacation = allVacation;
    }


    @Override
    public String getPeriod() {
        return super.getPeriod();
    }

}
