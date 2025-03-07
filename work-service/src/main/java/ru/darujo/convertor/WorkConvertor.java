package ru.darujo.convertor;

import ru.darujo.dto.WorkDto;
import ru.darujo.dto.WorkEditDto;
import ru.darujo.dto.WorkLittleDto;
import ru.darujo.model.Work;

public class WorkConvertor {
    public static WorkBuilder setWorkBulderBase(Work work){
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZI())
                .setDebugEndFact(work.getDebugEndFact())
                .setDevelopEndFact(work.getDevelopEndFact())
                .setOpeEndFact(work.getOpeEndFact())
                .setReleaseEndFact(work.getReleaseEndFact())
                .setAnaliseEndFact(work.getAnaliseEndFact())
                .setName(work.getName())
                .setTask(work.getTask())
                .setDescription(work.getDescription())
                .setPlanDateStage0(work.getPlanDateStage0())
                .setStartTaskPlan(work.getStartTaskPlan())
                .setStartTaskFact(work.getStartTaskFact())
                .setLaborDevelop(work.getLaborDevelop())
                .setLaborDebug(work.getLaborDebug())
                .setLaborRelease(work.getLaborRelease())
                .setLaborOPE(work.getLaborOPE())
                .setStageZI(work.getStageZI())
                .setRelease(work.getRelease())
                .setIssuingReleaseFact(work.getIssuingReleaseFact())
                .setIssuingReleasePlan(work.getIssuingReleasePlan());

    }
    public static WorkDto getWorkDto(Work work){
        return setWorkBulderBase(work)
                .getWorkDto();
    }
    public static WorkEditDto getWorkEditDto(Work work){
        return setWorkBulderBase(work)
                .setDebugEndPlan(work.getDebugEndPlan())
                .setDevelopEndPlan(work.getDevelopEndPlan())
                .setOpeEndPlan(work.getOpeEndPlan())
                .setReleaseEndPlan(work.getReleaseEndPlan())
                .setAnaliseEndPlan(work.getAnaliseEndPlan())
                .setFactDateStage0(work.getFactDateStage0())
                .getWorkEditDto();
    }
    public static Work getWork(WorkEditDto workDto){
        return WorkBuilder
                .createWork()
                .setId(workDto.getId())
                .setCodeSap(workDto.getCodeSap())
                .setCodeZI(workDto.getCodeZI())
                .setAnaliseEndFact(workDto.getAnaliseEndFact())
                .setReleaseEndFact(workDto.getReleaseEndFact())
                .setOpeEndFact(workDto.getOpeEndFact())
                .setDevelopEndFact(workDto.getDevelopEndFact())
                .setDebugEndFact(workDto.getDebugEndFact())
                .setName(workDto.getName())
                .setTask(workDto.getTask())
                .setDescription(workDto.getDescription())
                .setPlanDateStage0(workDto.getPlanDateStage0())
                .setStartTaskPlan(workDto.getStartTaskPlan())
                .setStartTaskFact(workDto.getStartTaskFact())
                .setLaborDevelop(workDto.getLaborDevelop())
                .setLaborDebug(workDto.getLaborDebug())
                .setLaborRelease(workDto.getLaborRelease())
                .setLaborOPE(workDto.getLaborOPE())
                .setStageZI(workDto.getStageZI())
                .setRelease(workDto.getRelease())
                .setIssuingReleaseFact(workDto.getIssuingReleaseFact())
                .setIssuingReleasePlan(workDto.getIssuingReleasePlan())
                .setDebugEndPlan(workDto.getDebugEndPlan())
                .setDevelopEndPlan(workDto.getDevelopEndPlan())
                .setOpeEndPlan(workDto.getOpeEndPlan())
                .setReleaseEndPlan(workDto.getReleaseEndPlan())
                .setAnaliseEndPlan(workDto.getAnaliseEndPlan())
                .setFactDateStage0(workDto.getFactDateStage0())
                .getWork();
    }

    public static WorkLittleDto getWorkLittleDto(Work work) {
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZI())
                .setStageZI(work.getStageZI())
                .setName(work.getName())
                .getWorkLittleDto();


    }
}
