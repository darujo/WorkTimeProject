package ru.darujo.dto.ratestage;

import ru.darujo.dto.workrep.ProjectUpdateInter;

import java.util.List;

public class RateDto implements ProjectUpdateInter {
    @SuppressWarnings("unused")
    public RateDto() {
    }

    private Long projectId;
    private String projectCode;
    private String projectName;
    private Boolean rated;
    private List<WorkStageDto> workStageDtoList;
    private List<WorkCriteriaDto> workCriteriaDtoList;
    private List<WorkTypeDto> workTypeDtoList;
    private AttrDto<Float> rateStatusSC;
    private AttrDto<Float> rateStatusST;
    private AttrDto<Float> rateStatusCT;

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public RateDto(Long projectId, Boolean rated, List<WorkStageDto> workStageDtoList, List<WorkCriteriaDto> workCriteriaDtoList, List<WorkTypeDto> workTypeDtoList, AttrDto<Float> rateStatusSC, AttrDto<Float> rateStatusST, AttrDto<Float> rateStatusCT) {
        this.projectId = projectId;
        this.rated = rated;
        this.workStageDtoList = workStageDtoList;
        this.workCriteriaDtoList = workCriteriaDtoList;
        this.workTypeDtoList = workTypeDtoList;
        this.rateStatusSC = rateStatusSC;
        this.rateStatusST = rateStatusST;
        this.rateStatusCT = rateStatusCT;
    }

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
    public Boolean getRated() {
        return rated;
    }

    @SuppressWarnings("unused")
    public List<WorkStageDto> getWorkStageDtoList() {
        return workStageDtoList;
    }

    @SuppressWarnings("unused")
    public List<WorkCriteriaDto> getWorkCriteriaDtoList() {
        return workCriteriaDtoList;
    }

    @SuppressWarnings("unused")
    public List<WorkTypeDto> getWorkTypeDtoList() {
        return workTypeDtoList;
    }

    @SuppressWarnings("unused")
    public AttrDto<Float> getRateStatusSC() {
        return rateStatusSC;
    }

    @SuppressWarnings("unused")
    public AttrDto<Float> getRateStatusST() {
        return rateStatusST;
    }

    @SuppressWarnings("unused")
    public AttrDto<Float> getRateStatusCT() {
        return rateStatusCT;
    }
}
