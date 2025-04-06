package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.model.WorkStage;

public class WorkStageBuilder {
    public static WorkStageBuilder createWorkStage () {
        return new WorkStageBuilder();
    }

    private Long id;
    private String nikName;
    private Integer role;
    private Float stage0;
    private Float stage1;
    private Float stage2;
    private Float stage3;
    private Float stage4;
    private Long workId;
    public WorkStageBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkStageBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public WorkStageBuilder setRole(Integer role) {
        this.role = role;
        return this;
    }

    public WorkStageBuilder setStage0(Float stage0) {
        this.stage0 = stage0;
        return this;
    }

    public WorkStageBuilder setStage1(Float stage1) {
        this.stage1 = stage1;
        return this;
    }

    public WorkStageBuilder setStage2(Float stage2) {
        this.stage2 = stage2;
        return this;
    }

    public WorkStageBuilder setStage3(Float stage3) {
        this.stage3 = stage3;
        return this;
    }

    public WorkStageBuilder setStage4(Float stage4) {
        this.stage4 = stage4;
        return this;
    }

    public WorkStageBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public WorkStageDto getWorkStateDto(){
        return new WorkStageDto(id,nikName,role,stage0,stage1,stage2,stage3,stage4,workId);
    }
    public WorkStage getWorkStage(){
        return new WorkStage(id,nikName,role,stage0,stage1,stage2,stage3,stage4,workId);
    }
}
