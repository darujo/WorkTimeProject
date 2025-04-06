package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.model.WorkStage;

public class WorkStageConvertor {
    public static WorkStageDto getWorkStageDto(WorkStage workStage){
        return WorkStageBuilder
                .createWorkStage()
                .setId(workStage.getId())
                .setNikName(workStage.getNikName())
                .setRole(workStage.getRole())
                .setStage0(workStage.getStage0())
                .setStage1(workStage.getStage1())
                .setStage2(workStage.getStage2())
                .setStage3(workStage.getStage3())
                .setStage4(workStage.getStage4())
                .setWorkId(workStage.getWorkId())
                .getWorkStateDto();
    }
    public static WorkStage getWorkStage(WorkStageDto workState){
        return WorkStageBuilder.createWorkStage()
                .setId(workState.getId())
                .setNikName(workState.getNikName())
                .setRole(workState.getRole())
                .setStage0(workState.getStage0())
                .setStage1(workState.getStage1())
                .setStage2(workState.getStage2())
                .setStage3(workState.getStage3())
                .setStage4(workState.getStage4())
                .setWorkId(workState.getWorkId())
                .getWorkStage();    }
}
