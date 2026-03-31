package ru.darujo.dto.work;

import java.io.Serializable;
import java.util.List;

public class WorkLittleDto implements Serializable {
    public WorkLittleDto() {
    }

    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование Зи
    private String name;
    private Integer stageZI;
    private Boolean rated;
    private List<Long> projectList;
    private WorkLittleDto parentWork;
    private List<WorkLittleDto> childWork;

    public WorkLittleDto(Long id, Long codeSap, String codeZI, String name, Integer stageZI, Boolean rated, List<Long> projectList, WorkLittleDto parentWork, List<WorkLittleDto> childWork) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.stageZI = stageZI;
        this.rated = rated;
        this.projectList = projectList;
        this.parentWork = parentWork;
        this.childWork = childWork;
    }

    @SuppressWarnings("unused")
    public Boolean getRated() {
        return rated;
    }

    @SuppressWarnings("unused")
    public Integer getStageZI() {
        return stageZI;
    }

    public Long getId() {
        return id;
    }

    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public List<Long> getProjectList() {
        return projectList;
    }

    @SuppressWarnings("unused")
    public WorkLittleDto getParentWork() {
        return parentWork;
    }

    @SuppressWarnings("unused")
    public List<WorkLittleDto> getChildWork() {
        return childWork;
    }

    public List<Long> getChildId() {
        return childWork == null || childWork.isEmpty() ? null : childWork.stream().map(WorkLittleDto::getId).toList();
    }
}
