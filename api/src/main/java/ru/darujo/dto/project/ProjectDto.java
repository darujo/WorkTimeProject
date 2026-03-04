package ru.darujo.dto.project;

import java.io.Serializable;

public class ProjectDto implements Serializable {

    private Long id;
    private String code;
    private String name;
    private Integer stageEnd;

    private String stage0Name;
    private String stage1Name;
    private String stage2Name;
    private String stage3Name;
    private String stage4Name;

    @SuppressWarnings("unused")
    public ProjectDto() {
    }

    public ProjectDto(Long id, String code, String name, Integer stageEnd, String stage0Name, String stage1Name, String stage2Name, String stage3Name, String stage4Name) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stageEnd = stageEnd;
        this.stage0Name = stage0Name;
        this.stage1Name = stage1Name;
        this.stage2Name = stage2Name;
        this.stage3Name = stage3Name;
        this.stage4Name = stage4Name;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public Integer getStageEnd() {
        return stageEnd;
    }

    public String getStage0Name() {
        return stage0Name;
    }

    public String getStage1Name() {
        return stage1Name;
    }

    public String getStage2Name() {
        return stage2Name;
    }

    public String getStage3Name() {
        return stage3Name;
    }

    public String getStage4Name() {
        return stage4Name;
    }

}
