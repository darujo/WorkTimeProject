package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.*;
import ru.darujo.service.ReleaseProjectService;

public class WorkConvertor {

    public static WorkBuilder setWorkBuilderBase(Work work, WorkProject workProject) {
        WorkBuilder workBuilder = WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setName(work.getName())
                .setDescription(work.getDescription())
                .setWorkParentDto(getWorkLittleDto(work.getWorkParent()))
                .setChildWorkDto(work.getChildWork() == null ? null : work.getChildWork().stream().map(WorkConvertor::getWorkLittleDto).toList());
        if (work.getRelease() != null) {
            workBuilder
                    .setReleaseId(work.getRelease().getId())
                    .setRelease(work.getRelease().getName())
                    .setIssuingReleasePlan(work.getRelease().getIssuingReleasePlan());
            if (workProject != null && workProject.getProjectId() != null) {
                ReleaseProject releaseProject = ReleaseProjectService.getINSTANCE().findReleaseProject(work.getRelease(),
                        workProject.getProjectId());
                if (releaseProject != null) {
                    workBuilder.setIssuingReleaseFact(releaseProject.getIssuingReleaseFact());
                }
            }
        }
        if (workProject != null) {
            if (work.getRelease() != null) {
                ReleaseProject releaseProject = ReleaseProjectService.getINSTANCE().findReleaseProject(work.getRelease(), workProject.getProjectId());
                if (releaseProject != null) {
                    workBuilder.setIssuingReleaseFact(releaseProject.getIssuingReleaseFact());
                }
            }
            workBuilder
                    .setWorkProjectId(workProject.getId())
                    .setDebugEndFact(workProject.getDebugEndFact())
                    .setDevelopEndFact(workProject.getDevelopEndFact())
                    .setIssuePrototypeFact(workProject.getIssuePrototypeFact())
                    .setOpeEndFact(workProject.getOpeEndFact())
                    .setReleaseEndFact(workProject.getReleaseEndFact())
                    .setAnaliseEndFact(workProject.getAnaliseEndFact())
                    .setAnaliseEndPlan(workProject.getAnaliseEndPlan())
                    .setTask(workProject.getTask())
                    .setRated(workProject.getRated())
                    .setStartTaskPlan(workProject.getStartTaskPlan())
                    .setStartTaskFact(workProject.getStartTaskFact())
                    .setStageZI(workProject.getStageZi())
                    .setProjectId(workProject.getProjectId());
        }

        return workBuilder;
    }

    public static WorkDto getWorkDto(WorkFull workFull) {
        return setWorkBuilderBase(workFull.getWork(), workFull.getWorkProject())
                .setChildWork(workFull.getWork().getChildWork())
                .getWorkDto();
    }

    public static WorkEditDto getWorkEditDto(WorkFull workFull) {
        Work work = workFull.getWork();
        WorkProject workProject = workFull.getWorkProject();
        WorkBuilder workBuilder = setWorkBuilderBase(work, workProject)
                .setProjectList(work.getProjectList());
        if (workProject != null) {
            workBuilder.setDebugEndPlan(workProject.getDebugEndPlan())
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
                    .setOpeStartFact(workProject.getOpeStartFact());
        }
        return workBuilder.getWorkEditDto();
    }

    public static WorkFull getWork(WorkEditDto workDto, Long projectId) {
        WorkProject workProject = WorkBuilder
                .createWork()
                .setWorkParent(getWorkLittle(workDto.getParentWork()))
                .setChildWork(workDto.getChildWork() == null ? null : workDto.getChildWork().stream().map(WorkConvertor::getWorkLittle).toList())

                .setId(workDto.getId())
                .setWorkProjectId(workDto.getWorkProjectId())
                .setCodeSap(workDto.getCodeSap())
                .setCodeZI(workDto.getCodeZI())
                .setAnaliseEndFact(DateHelper.zDTToLD(workDto.getAnaliseEndFact()))
                .setReleaseEndFact(DateHelper.zDTToLD(workDto.getReleaseEndFact()))
                .setOpeEndFact(DateHelper.zDTToLD(workDto.getOpeEndFact()))
                .setDevelopEndFact(DateHelper.zDTToLD(workDto.getDevelopEndFact()))
                .setIssuePrototypeFact(DateHelper.zDTToLD(workDto.getIssuePrototypeFact()))
                .setDebugEndFact(DateHelper.zDTToLD(workDto.getDebugEndFact()))
                .setName(workDto.getName())
                .setTask(workDto.getTask())
                .setRated(workDto.getRated())
                .setDescription(workDto.getDescription())
                .setStartTaskPlan(DateHelper.zDTToLD(workDto.getStartTaskPlan()))
                .setStartTaskFact(DateHelper.zDTToLD(workDto.getStartTaskFact()))
                .setStageZI(workDto.getStageZI())
                .setReleaseId(workDto.getReleaseId())
                .setIssuingReleaseFact(DateHelper.zDTToLD(workDto.getIssuingReleaseFact()))
                .setIssuingReleasePlan(DateHelper.zDTToLD(workDto.getIssuingReleasePlan()))
                .setAnaliseEndPlan(DateHelper.zDTToLD(workDto.getAnaliseEndPlan()))
                .setDebugEndPlan(DateHelper.zDTToLD(workDto.getDebugEndPlan()))
                .setDevelopEndPlan(DateHelper.zDTToLD(workDto.getDevelopEndPlan()))
                .setIssuePrototypePlan(DateHelper.zDTToLD(workDto.getIssuePrototypePlan()))
                .setReleaseEndPlan(DateHelper.zDTToLD(workDto.getReleaseEndPlan()))
                .setOpeEndPlan(DateHelper.zDTToLD(workDto.getOpeEndPlan()))
                .setRelease(workDto.getRelease())
                .setAnaliseStartPlan(DateHelper.zDTToLD(workDto.getAnaliseStartPlan()))
                .setDevelopStartPlan(DateHelper.zDTToLD(workDto.getDevelopStartPlan()))
                .setDebugStartPlan(DateHelper.zDTToLD(workDto.getDebugStartPlan()))
                .setReleaseStartPlan(DateHelper.zDTToLD(workDto.getReleaseStartPlan()))
                .setOpeStartPlan(DateHelper.zDTToLD(workDto.getOpeStartPlan()))

                .setAnaliseStartFact(DateHelper.zDTToLD(workDto.getAnaliseStartFact()))
                .setDevelopStartFact(DateHelper.zDTToLD(workDto.getDevelopStartFact()))
                .setDebugStartFact(DateHelper.zDTToLD(workDto.getDebugStartFact()))
                .setReleaseStartFact(DateHelper.zDTToLD(workDto.getReleaseStartFact()))
                .setOpeStartFact(DateHelper.zDTToLD(workDto.getOpeStartFact()))
                .setProjectList(workDto.getProjectList())
                .setProjectId(projectId).getWorkProject();
        return new WorkFull(workProject.getWork(), workProject);
    }

    public static WorkLittleDto getWorkLittleDto(WorkLittleFull workLittleFull) {
        return getWorkLittleDto(workLittleFull.getWork(), workLittleFull.getWorkProject(), true);
    }

    public static WorkLittleDto getWorkLittleDto(WorkLittle work) {
        if (work == null) {
            return null;
        }
        return getWorkLittleDto(work, null, false);
    }

    public static WorkLittleDto getWorkLittleDto(WorkLittle work, WorkProjectLittle workProjectLittle, boolean loadAll) {
        WorkBuilder workBuilder = WorkBuilder
                .createWork()
                .setId(work.getId())
                .setWorkProjectId(workProjectLittle == null ? null : workProjectLittle.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setStageZI(workProjectLittle == null ? null : workProjectLittle.getStageZi())
                .setName(work.getName())
                .setRated(workProjectLittle == null ? null : workProjectLittle.getRated())
                .setProjectList(work.getProjectList());
        if (loadAll) {
            workBuilder.setWorkParentDto(getWorkLittleDto(work.getWorkParent()))
                    .setChildWorkDto(work.getChildWork() == null ? null : work.getChildWork().stream().map(WorkConvertor::getWorkLittleDto).toList());
        }
        return workBuilder.getWorkLittleDto();


    }


    public static WorkLittleDto getWorkLittleDto(Work work) {
        return getWorkLittleDto(work, true);


    }

    public static WorkLittleDto getWorkLittleDto(Work work, boolean loadAll) {
        WorkBuilder workBuilder = WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setCodeZI(work.getCodeZi())
                .setName(work.getName())
                .setProjectList(work.getProjectList());
        if (loadAll) {
            workBuilder.setWorkParentDto(getWorkLittleDto(work.getWorkParent()))
                    .setChildWorkDto(work.getChildWork() == null ? null : work.getChildWork().stream().map(WorkConvertor::getWorkLittleDto).toList());
        }
        return workBuilder.getWorkLittleDto();
    }

    public static WorkLittle getWorkLittle(WorkLittleDto work) {
        if (work == null) {
            return null;
        }
        return WorkBuilder
                .createWork()
                .setId(work.getId())
                .setCodeSap(work.getCodeSap())
                .setName(work.getName())
                .setProjectList(work.getProjectList())
                .getWorkParent();


    }

}
