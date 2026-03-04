package ru.darujo.dto.workrep;

import ru.darujo.dto.work.WorkLittleDto;

import java.io.Serializable;
import java.util.List;

public class WorkGraphDto implements Serializable {
    @SuppressWarnings("unused")
    public WorkGraphDto() {
    }

    private WorkLittleDto workLittleDto;
    private List<ProjectGraphDto> projectGraphList;

    public WorkGraphDto(WorkLittleDto workLittleDto, List<ProjectGraphDto> projectGraphList) {
        this.workLittleDto = workLittleDto;
        this.projectGraphList = projectGraphList;
    }

    @SuppressWarnings("unused")
    public WorkLittleDto getWorkLittleDto() {
        return workLittleDto;
    }

    @SuppressWarnings("unused")
    public List<ProjectGraphDto> getProjectGraphList() {
        return projectGraphList;
    }
}
