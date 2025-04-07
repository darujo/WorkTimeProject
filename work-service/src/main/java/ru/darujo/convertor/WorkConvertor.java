package ru.darujo.convertor;

import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;

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
                .setAnaliseEndPlan(work.getAnaliseEndPlan())
                .setName(work.getName())
                .setTask(work.getTask())
                .setDescription(work.getDescription())
                .setStartTaskPlan(work.getStartTaskPlan())
                .setStartTaskFact(work.getStartTaskFact())
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
                .setStartTaskPlan(workDto.getStartTaskPlan())
                .setStartTaskFact(workDto.getStartTaskFact())
                .setStageZI(workDto.getStageZI())
                .setRelease(workDto.getRelease())
                .setIssuingReleaseFact(workDto.getIssuingReleaseFact())
                .setIssuingReleasePlan(workDto.getIssuingReleasePlan())
                .setDebugEndPlan(workDto.getDebugEndPlan())
                .setDevelopEndPlan(workDto.getDevelopEndPlan())
                .setOpeEndPlan(workDto.getOpeEndPlan())
                .setReleaseEndPlan(workDto.getReleaseEndPlan())
                .setAnaliseEndPlan(workDto.getAnaliseEndPlan())
                .getWork();
    }

    public static WorkLittleDto getWorkLittleDto(WorkLittle work) {
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
