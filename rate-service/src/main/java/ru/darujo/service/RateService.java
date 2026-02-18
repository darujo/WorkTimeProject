package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.convertor.WorkCriteriaConvertor;
import ru.darujo.convertor.WorkStageConvertor;
import ru.darujo.convertor.WorkTypeConvertor;
import ru.darujo.dto.project.ProjectDto;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.RateDto;
import ru.darujo.dto.ratestage.WorkRateDto;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.dto.workrep.ProjectUpdateInter;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.UserServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkCriteria;
import ru.darujo.model.WorkStage;
import ru.darujo.model.WorkType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
public class RateService {
    private UserServiceIntegration userServiceIntegration;
    private WorkStageService workStageService;
    private WorkCriteriaService workCriteriaService;
    private WorkTypeService workTypeService;
    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setUserServiceIntegration(UserServiceIntegration userServiceIntegration) {
        this.userServiceIntegration = userServiceIntegration;
    }

    @Autowired
    public void setWorkStageService(WorkStageService workStageService) {
        this.workStageService = workStageService;
    }

    @Autowired
    public void setWorkCriteriaService(WorkCriteriaService workCriteriaService) {
        this.workCriteriaService = workCriteriaService;
    }

    @Autowired
    public void setWorkTypeService(WorkTypeService workTypeService) {
        this.workTypeService = workTypeService;
    }

    public Float getTimeStageNotAnalise(Long workId, Long projectId) {
        AtomicReference<Float> timeStage = new AtomicReference<>();
        timeStage.set(0f);
        List<WorkStage> workStageList = workStageService.findWorkStage(workId, projectId);
        workStageList.forEach(
                workStage -> timeStage.set(timeStage.get()
                        + getTime(workStage.getStage0())
                        + getTime(workStage.getStage1())
                        + getTime(workStage.getStage2())
                        + getTime(workStage.getStage3())
                        + getTime(workStage.getStage4())
                ));
        return timeStage.get();
    }


    public Float getTimeCriteria(Long workId, Long projectId) {
        AtomicReference<Float> timeCriteria = new AtomicReference<>();

        timeCriteria.set(0f);

        List<WorkCriteria> workCriteriaList = workCriteriaService.findWorkCriteria(workId, projectId);
        workCriteriaList.forEach(
                workCriteria -> timeCriteria.set(
                        timeCriteria.get() + getTime(workCriteria.getDevelop10()) + getTime(workCriteria.getDevelop50()) + getTime(workCriteria.getDevelop100())));
        return timeCriteria.get();

    }

    public Float getTimeType(Long workId, Long projectId) {
        AtomicReference<Float> timeType = new AtomicReference<>();

        timeType.set(0f);

        List<WorkType> workTypeList = workTypeService.findWorkCriteria(workId, projectId);
        workTypeList.forEach(
                workType -> timeType.set(
                        timeType.get() + getTime(workType.getTime())));
        return timeType.get();

    }

    public AttrDto<Float> comparisonStageCriteria(Long workId, Long projectId) {
        Float timeStage = getTimeStageNotAnalise(workId, projectId);
        Float timeCriteria = getTimeCriteria(workId, projectId);
        float time = timeCriteria - timeStage;
        if (time == 0 || timeStage == 0 || timeCriteria == 0) {
            return new AttrDto<>(time, "");
        }
        if (time < 0) {
            return new AttrDto<>(time, "Плановой оценки больше чем критериев на " + time * -1);
        }
        return new AttrDto<>(time, "Критериев больше чем плановой оценки на " + time);
    }

    public AttrDto<Float> comparisonCriteriaType(Long workId, Long projectId) {
        Float timeType = getTimeType(workId, projectId);
        Float timeCriteria = getTimeCriteria(workId, projectId);
        float time = timeCriteria - timeType;
        if (time == 0 || timeType == 0 || timeCriteria == 0) {
            return new AttrDto<>(time, "");
        }
        if (time < 0) {
            return new AttrDto<>(time, "Работ больше чем критериев на " + time * -1);
        }
        return new AttrDto<>(time, "Критериев больше чем работ на " + time);
    }

    public AttrDto<Float> comparisonStageType(Long workId, Long projectId) {
        Float timeStage = getTimeStageNotAnalise(workId, projectId);
        Float timeType = getTimeType(workId, projectId);
        float time = timeType - timeStage;
        if (time == 0 || timeStage == 0 || timeType == 0) {
            return new AttrDto<>(time, "");
        }
        if (time < 0) {
            return new AttrDto<>(time, "Плановой оценки больше чем работ на " + time * -1);
        }

        return new AttrDto<>(time, "Работ больше чем плановой оценки на " + time);
    }

    private Float getTime(Float time) {
        if (time == null) {
            return 0f;
        }
        return time;
    }

    public WorkStageDto AllTime(Long workId, Long projectId, boolean loadFact) {
        if (loadFact) {
            throw new ResourceNotFoundRunTime("Загрузка с фактом не поддерживается");
        }
        final Float[] stage = new Float[5];
        stage[0] = 0f;
        stage[1] = 0f;
        stage[2] = 0f;
        stage[3] = 0f;
        stage[4] = 0f;

        workStageService.findWorkStage(workId, projectId).forEach(workStage -> {
            stage[0] = stage[0] + getTime(workStage.getStage0());
            stage[1] = stage[1] + getTime(workStage.getStage1());
            stage[2] = stage[2] + getTime(workStage.getStage2());
            stage[3] = stage[3] + getTime(workStage.getStage3());
            stage[4] = stage[4] + getTime(workStage.getStage4());
        });

        return new WorkStageDto(
                -1L,
                null,
                -1,
                stage[0],
                stage[1],
                stage[2],
                stage[3],
                stage[4],
                workId,
                projectId);
    }

    public WorkRateDto getRate(Long workId) {
        WorkLittleDto workLittleDto = workServiceIntegration.getWorEditDto(workId);
        List<RateDto> rateDtoList = new ArrayList<>();
        List<WorkStageDto> workStageDtoListTotal = new ArrayList<>();
        workLittleDto.getProjectList().forEach(projectId -> {
            List<WorkStageDto> workStageList = new ArrayList<>();
            workStageService.findWorkStage(workId, projectId).forEach(workStage -> workStageList.add(WorkStageConvertor.getWorkStageDto(workStage)));
            workStageService.updWorkStage(workId, workStageList, projectId);
            workStageList.forEach(workStageDto -> workStageService.updFio(workStageDto));
            WorkStageDto workStageDto = getTotal(workStageList);

            workStageList.add(workStageDto);
            workStageDtoListTotal.add(workStageDto);
            RateDto rateDto = new RateDto(projectId,
                    workServiceIntegration.getRate(workId, projectId),
                    workStageList,
                    workCriteriaService.findWorkCriteria(workId, projectId).stream().map(WorkCriteriaConvertor::getWorkCriteriaDto).toList(),
                    workTypeService.findWorkCriteria(workId, projectId).stream().map(WorkTypeConvertor::getWorkTypeDto).toList(),
                    comparisonStageCriteria(workId, projectId),
                    comparisonStageType(workId, projectId),
                    comparisonCriteriaType(workId, projectId)
            );
            updateProject(rateDto);
            rateDtoList.add(rateDto);
        });
        return new WorkRateDto(workLittleDto.getName(), workLittleDto.getCodeSap(), workLittleDto.getCodeZI(), rateDtoList, getTotal(workStageDtoListTotal));
    }

    private WorkStageDto getTotal(List<WorkStageDto> workStageDtoList) {
        WorkStageDto workStageDtoTotal = new WorkStageDto(null, "Итого", null, 0f, 0f, 0f, 0f, 0f, null, null);
        workStageDtoTotal.setStage0Fact(0f);
        workStageDtoTotal.setStage1Fact(0f);
        workStageDtoTotal.setStage2Fact(0f);
        workStageDtoTotal.setStage3Fact(0f);
        workStageDtoTotal.setStage4Fact(0f);
        workStageDtoTotal.setStage5Fact(0f);

        workStageDtoList.forEach(workStageDto -> {
            workStageDtoTotal.setStage0(workStageDtoTotal.getStage0() + floatNotNull(workStageDto.getStage0()));
            workStageDtoTotal.setStage1(workStageDtoTotal.getStage1() + floatNotNull(workStageDto.getStage1()));
            workStageDtoTotal.setStage2(workStageDtoTotal.getStage2() + floatNotNull(workStageDto.getStage2()));
            workStageDtoTotal.setStage3(workStageDtoTotal.getStage3() + floatNotNull(workStageDto.getStage3()));
            workStageDtoTotal.setStage4(workStageDtoTotal.getStage4() + floatNotNull(workStageDto.getStage4()));
            workStageDtoTotal.setStage0Fact(workStageDtoTotal.getStage0Fact() + floatNotNull(workStageDto.getStage0Fact()));
            workStageDtoTotal.setStage1Fact(workStageDtoTotal.getStage1Fact() + floatNotNull(workStageDto.getStage1Fact()));
            workStageDtoTotal.setStage2Fact(workStageDtoTotal.getStage2Fact() + floatNotNull(workStageDto.getStage2Fact()));
            workStageDtoTotal.setStage3Fact(workStageDtoTotal.getStage3Fact() + floatNotNull(workStageDto.getStage3Fact()));
            workStageDtoTotal.setStage4Fact(workStageDtoTotal.getStage4Fact() + floatNotNull(workStageDto.getStage4Fact()));
            workStageDtoTotal.setStage5Fact(workStageDtoTotal.getStage5Fact() + floatNotNull(workStageDto.getStage5Fact()));

        });
        workStageDtoTotal.setStageAll(workStageDtoTotal.getStage0()
                + workStageDtoTotal.getStage1()
                + workStageDtoTotal.getStage2()
                + workStageDtoTotal.getStage3()
                + workStageDtoTotal.getStage4());
        return workStageDtoTotal;
    }
    private Float floatNotNull(Float time){
        if (time == null){
            return 0f;
        }
        return time;
    }
    private static final Map<Long, ProjectDto> projectDtoMap = new HashMap<>();

    public void init() {
        try {
            userServiceIntegration.getProjects(null, null).forEach(projectDto ->
                    projectDtoMap.put(projectDto.getId(), projectDto));
        } catch (RuntimeException ignore) {

        }

    }

    public Map<Long, ProjectDto> getProjectDtoMap() {
        projectDtoMap.clear();
        init();
        return projectDtoMap;
    }

    public void updateProject(ProjectUpdateInter projectUpdateInter) {
        ProjectDto projectDto = getProjectDtoMap().get(projectUpdateInter.getProjectId());
        projectUpdateInter.setProjectName(projectDto.getName());
        projectUpdateInter.setProjectCode(projectDto.getCode());
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }
}
