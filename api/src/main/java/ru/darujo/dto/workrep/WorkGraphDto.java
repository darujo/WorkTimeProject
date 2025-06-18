package ru.darujo.dto.workrep;

import ru.darujo.dto.work.WorkLittleDto;

import java.util.List;

public class WorkGraphDto{

    WorkLittleDto workLittleDto;
    List<WorkPeriodColorDto> workPeriodColorPlanDTOs;
    List<WorkPeriodColorDto> workPeriodColorFactDTOs;

    public WorkGraphDto(WorkLittleDto workLittleDto, List<WorkPeriodColorDto> workPeriodColorPlanDTOs, List<WorkPeriodColorDto> workPeriodColorFactDTOs) {
        this.workLittleDto = workLittleDto;
        this.workPeriodColorPlanDTOs = workPeriodColorPlanDTOs;
        this.workPeriodColorFactDTOs = workPeriodColorFactDTOs;
    }

    @SuppressWarnings("unused")
    public WorkLittleDto getWorkLittleDto() {
        return workLittleDto;
    }

    @SuppressWarnings("unused")
    public List<WorkPeriodColorDto> getWorkPeriodColorPlanDtos() {
        return workPeriodColorPlanDTOs;
    }
    @SuppressWarnings("unused")
    public List<WorkPeriodColorDto> getWorkPeriodColorFactDtos() {
        return workPeriodColorFactDTOs;
    }
}
