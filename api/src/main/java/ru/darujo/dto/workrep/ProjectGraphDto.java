package ru.darujo.dto.workrep;

import java.io.Serializable;
import java.util.List;

public class ProjectGraphDto implements ProjectUpdateInter, Serializable {
    @SuppressWarnings("unused")
    public ProjectGraphDto() {
    }

    private Long projectId;
    private String projectCode;
    private String projectName;
    private List<WorkPeriodColorDto> workPeriodColorPlanDTOs;
    private List<WorkPeriodColorDto> workPeriodColorFactDTOs;

    public ProjectGraphDto(Long projectId, List<WorkPeriodColorDto> workPeriodColorPlanDTOs, List<WorkPeriodColorDto> workPeriodColorFactDTOs) {
        this.projectId = projectId;
        this.workPeriodColorPlanDTOs = workPeriodColorPlanDTOs;
        this.workPeriodColorFactDTOs = workPeriodColorFactDTOs;
    }

    @SuppressWarnings("unused")
    @Override
    public Long getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unused")
    public String getProjectCode() {
        return projectCode;
    }

    @SuppressWarnings("unused")
    public String getProjectName() {
        return projectName;
    }

    @SuppressWarnings("unused")
    public List<WorkPeriodColorDto> getWorkPeriodColorPlanDTOs() {
        return workPeriodColorPlanDTOs;
    }

    @SuppressWarnings("unused")
    public List<WorkPeriodColorDto> getWorkPeriodColorFactDTOs() {
        return workPeriodColorFactDTOs;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
