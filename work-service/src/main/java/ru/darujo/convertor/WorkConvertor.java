package ru.darujo.convertor;

import ru.darujo.dto.WorkDto;
import ru.darujo.dto.WorkEditDto;
import ru.darujo.model.Work;

public class WorkConvertor {
    public static WorkBuilder setWorkBulderBase(Work work){
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZI())
                .setDateStartDebug(work.getDateStartDebug())
                .setDateStartDevelop(work.getDateStartDevelop())
                .setDateStartOPE(work.getDateStartOPE())
                .setDateStartRelease(work.getDateStartRelease())
                .setDateStartWender(work.getDateStartWender())
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
                .getWorkEditDto();
    }
    public static Work getWork(WorkEditDto workDto){
        return WorkBuilder
                .createWork()
                .setId(workDto.getId())
                .setCodeSap(workDto.getCodeSap())
                .setCodeZI(workDto.getCodeZI())
                .setDateStartWender(workDto.getDateStartWender())
                .setDateStartRelease(workDto.getDateStartRelease())
                .setDateStartOPE(workDto.getDateStartOPE())
                .setDateStartDevelop(workDto.getDateStartDevelop())
                .setDateStartDebug(workDto.getDateStartDebug())
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
                .getWork();
    }
}
