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
                .setIssuePrototypeFact(work.getIssuePrototypeFact())
                .setOpeEndFact(work.getOpeEndFact())
                .setReleaseEndFact(work.getReleaseEndFact())
                .setAnaliseEndFact(work.getAnaliseEndFact())
                .setAnaliseEndPlan(work.getAnaliseEndPlan())
                .setName(work.getName())
                .setTask(work.getTask())
                .setRated(work.getRated())
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
                .setIssuePrototypePlan(work.getIssuePrototypePlan())
                .setOpeEndPlan(work.getOpeEndPlan())
                .setReleaseEndPlan(work.getReleaseEndPlan())

                .setAnaliseStartPlan(work.getAnaliseStartPlan())
                .setDevelopStartPlan(work.getDevelopStartPlan())
                .setDebugStartPlan(work.getDebugStartPlan())
                .setReleaseStartPlan(work.getReleaseStartPlan())
                .setOpeStartPlan(work.getOpeStartPlan())

                .setAnaliseStartFact(work.getAnaliseStartFact())
                .setDevelopStartFact(work.getDevelopStartFact())
                .setDebugStartFact(work.getDebugStartFact())
                .setReleaseStartFact(work.getReleaseStartFact())
                .setOpeStartFact(work.getOpeStartFact())

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
                .setIssuePrototypeFact(workDto.getIssuePrototypeFact())
                .setDebugEndFact(workDto.getDebugEndFact())
                .setName(workDto.getName())
                .setTask(workDto.getTask())
                .setRated(workDto.getRated())
                .setDescription(workDto.getDescription())
                .setStartTaskPlan(workDto.getStartTaskPlan())
                .setStartTaskFact(workDto.getStartTaskFact())
                .setStageZI(workDto.getStageZI())
                .setReleaseId(workDto.getReleaseId())
                .setIssuingReleaseFact(workDto.getIssuingReleaseFact())
                .setIssuingReleasePlan(workDto.getIssuingReleasePlan())
                .setAnaliseEndPlan(workDto.getAnaliseEndPlan())
                .setDebugEndPlan(workDto.getDebugEndPlan())
                .setDevelopEndPlan(workDto.getDevelopEndPlan())
                .setIssuePrototypePlan(workDto.getIssuePrototypePlan())
                .setReleaseEndPlan(workDto.getReleaseEndPlan())
                .setOpeEndPlan(workDto.getOpeEndPlan())
                .setRelease(workDto.getRelease())
                .setAnaliseStartPlan(workDto.getAnaliseStartPlan())
                .setDevelopStartPlan(workDto.getDevelopStartPlan())
                .setDebugStartPlan(workDto.getDebugStartPlan())
                .setReleaseStartPlan(workDto.getReleaseStartPlan())
                .setOpeStartPlan(workDto.getOpeStartPlan())

                .setAnaliseStartFact(workDto.getAnaliseStartFact())
                .setDevelopStartFact(workDto.getDevelopStartFact())
                .setDebugStartFact(workDto.getDebugStartFact())
                .setReleaseStartFact(workDto.getReleaseStartFact())
                .setOpeStartFact(workDto.getOpeStartFact())

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
