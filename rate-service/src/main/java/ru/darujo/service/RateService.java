package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.WorkCriteria;
import ru.darujo.model.WorkStage;
import ru.darujo.model.WorkType;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
public class RateService {
    WorkStageService workStageService;
    WorkCriteriaService workCriteriaService;
    WorkTypeService workTypeService;

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

    public AttrDto<Float> ComparisonStageCriteria(Long workId, Long projectId) {
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

    public AttrDto<Float> ComparisonCriteriaType(Long workId, Long projectId) {
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

    public AttrDto<Float> ComparisonStageType(Long workId, Long projectId) {
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
}
