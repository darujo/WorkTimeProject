package ru.darujo.convertor;

import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;

public class WorkConvertor {
    public static WorkBuilder setWorkBuilderBase(Work work) {
        WorkBuilder workBuilder = WorkBuilder
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
                .setStageZI(work.getStageZI());
        if (work.getRelease() != null) {
            workBuilder
                    .setReleaseId(work.getRelease().getId())
                    .setRelease(work.getRelease().getName())
                    .setIssuingReleaseFact(work.getRelease().getIssuingReleaseFact())
                    .setIssuingReleasePlan(work.getRelease().getIssuingReleasePlan());
        }
        return workBuilder;
    }

    public static WorkDto getWorkDto(Work work) {
        return setWorkBuilderBase(work)
                .getWorkDto();
    }

    public static WorkEditDto getWorkEditDto(Work work) {
        return setWorkBuilderBase(work)
                .setDebugEndPlan(work.getDebugEndPlan())
                .setDevelopEndPlan(work.getDevelopEndPlan())
                .setOpeEndPlan(work.getOpeEndPlan())
                .setReleaseEndPlan(work.getReleaseEndPlan())
                .getWorkEditDto();
    }

    public static Work getWork(WorkEditDto workDto) {
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
                .setReleaseId(workDto.getReleaseId())
                .setIssuingReleaseFact(workDto.getIssuingReleaseFact())
                .setIssuingReleasePlan(workDto.getIssuingReleasePlan())
                .setDebugEndPlan(workDto.getDebugEndPlan())
                .setDevelopEndPlan(workDto.getDevelopEndPlan())
                .setOpeEndPlan(workDto.getOpeEndPlan())
                .setReleaseId(workDto.getReleaseId())
                .setRelease(workDto.getRelease())
                .setIssuingReleaseFact(workDto.getIssuingReleaseFact())
                .setIssuingReleasePlan(workDto.getIssuingReleasePlan())
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
