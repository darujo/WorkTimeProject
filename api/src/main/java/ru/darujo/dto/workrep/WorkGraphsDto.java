package ru.darujo.dto.workrep;

import ru.darujo.dto.calendar.WeekWorkDto;

import java.io.Serializable;
import java.util.List;

public class WorkGraphsDto implements Serializable {
    List<WeekWorkDto> weekWorkDTOs;
    List<WorkGraphDto> workGraphDTOs;

    public WorkGraphsDto(List<WeekWorkDto> weekWorkDTOs, List<WorkGraphDto> workGraphDTOs) {
        this.weekWorkDTOs = weekWorkDTOs;
        this.workGraphDTOs = workGraphDTOs;
    }

    @SuppressWarnings("unused")
    public List<WeekWorkDto> getWeekWorkDTOs() {
        return weekWorkDTOs;
    }

    @SuppressWarnings("unused")
    public List<WorkGraphDto> getWorkGraphDTOs() {
        return workGraphDTOs;
    }
}
