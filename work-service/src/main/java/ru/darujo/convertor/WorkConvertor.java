package ru.darujo.convertor;

import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.*;

public class WorkConvertor {

    public static WorkBuilder setWorkBuilderBase(Work work, WorkProject workProject) {
        WorkBuilder workBuilder = WorkBuilder
                .createWork()
                .setId(work.getId())
                .setWorkProjectId(workProject.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setDebugEndFact(workProject.getDebugEndFact())
                .setDevelopEndFact(workProject.getDevelopEndFact())
                .setIssuePrototypeFact(workProject.getIssuePrototypeFact())
                .setOpeEndFact(workProject.getOpeEndFact())
                .setReleaseEndFact(workProject.getReleaseEndFact())
                .setAnaliseEndFact(workProject.getAnaliseEndFact())
                .setAnaliseEndPlan(workProject.getAnaliseEndPlan())
                .setName(work.getName())
                .setTask(workProject.getTask())
                .setRated(workProject.getRated())
                .setDescription(work.getDescription())
                .setStartTaskPlan(workProject.getStartTaskPlan())
                .setStartTaskFact(workProject.getStartTaskFact())
                .setStageZI(workProject.getStageZi());
        if (workProject.getRelease() != null) {
            workBuilder
                    .setReleaseId(workProject.getRelease().getId())
                    .setRelease(workProject.getRelease().getName())
                    .setIssuingReleaseFact(workProject.getRelease().getIssuingReleaseFact())
                    .setIssuingReleasePlan(workProject.getRelease().getIssuingReleasePlan());
        }
        return workBuilder;
    }

    public static WorkDto getWorkDto(WorkFull workFull) {
        return setWorkBuilderBase(workFull.getWork(), workFull.getWorkProject())
                .getWorkDto();
    }

    public static WorkEditDto getWorkEditDto(WorkFull workFull) {
        Work work = workFull.getWork();
        WorkProject workProject = workFull.getWorkProject();
        return setWorkBuilderBase(work, workProject)
                .setDebugEndPlan(workProject.getDebugEndPlan())
                .setDevelopEndPlan(workProject.getDevelopEndPlan())
                .setIssuePrototypePlan(workProject.getIssuePrototypePlan())
                .setOpeEndPlan(workProject.getOpeEndPlan())
                .setReleaseEndPlan(workProject.getReleaseEndPlan())

                .setAnaliseStartPlan(workProject.getAnaliseStartPlan())
                .setDevelopStartPlan(workProject.getDevelopStartPlan())
                .setDebugStartPlan(workProject.getDebugStartPlan())
                .setReleaseStartPlan(workProject.getReleaseStartPlan())
                .setOpeStartPlan(workProject.getOpeStartPlan())

                .setAnaliseStartFact(workProject.getAnaliseStartFact())
                .setDevelopStartFact(workProject.getDevelopStartFact())
                .setDebugStartFact(workProject.getDebugStartFact())
                .setReleaseStartFact(workProject.getReleaseStartFact())
                .setOpeStartFact(workProject.getOpeStartFact())
                .setProjectList(work.getProjectList())
                .setProjectId(workProject.getProjectId())
                .getWorkEditDto();
    }

    public static WorkFull getWork(WorkEditDto workDto, Long projectId) {
        WorkProject workProject = WorkBuilder
                .createWork()
                .setId(workDto.getId())
                .setWorkProjectId(workDto.getWorkProjectId())
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
                .setProjectList(workDto.getProjectList())
                .setProjectId(projectId).getWorkProject();
        return new WorkFull(workProject.getWork(), workProject);
    }

    public static WorkLittleDto getWorkLittleDto(WorkLittleFull workLittleFull) {
        WorkLittle work = workLittleFull.getWork();
        WorkProjectLittle workProjectLittle = workLittleFull.getWorkProject();
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setWorkProjectId(workProjectLittle == null ? null : workProjectLittle.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setStageZI(workProjectLittle == null ? null : workProjectLittle.getStageZi())
                .setName(work.getName())
                .setRated(workProjectLittle == null ? null : workProjectLittle.getRated())
                .setProjectList(work.getProjectList())
                .getWorkLittleDto();


    }

    public static WorkLittleDto getWorkLittleDto(Work work) {
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setName(work.getName())
                .setProjectList(work.getProjectList())
                .getWorkLittleDto();


    }

}
