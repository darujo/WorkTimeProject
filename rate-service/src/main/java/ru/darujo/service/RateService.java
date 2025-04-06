package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.dto.ratestage.WorkStageDto;
import ru.darujo.model.WorkCriteria;
import ru.darujo.model.WorkStage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
public class RateService {
    WorkStageService workStageService;
    WorkCriteriaService workCriteriaService;

    @Autowired
    public void setWorkStageService(WorkStageService workStageService) {
        this.workStageService = workStageService;
    }

    @Autowired
    public void setWorkCriteriaService(WorkCriteriaService workCriteriaService) {
        this.workCriteriaService = workCriteriaService;
    }

    public AttrDto ComparisonStageCriteria(Long workId) {
        AtomicReference<Float> timeCriteria = new AtomicReference<>();
        AtomicReference<Float> timeStage = new AtomicReference<>();
        timeCriteria.set( 0f);
        timeStage.set( 0f);
        List<WorkCriteria> workCriteriaList = workCriteriaService.findWorkCriteria(workId);
        workCriteriaList.forEach(workCriteria -> timeCriteria.set(timeCriteria.get() + workCriteria.getDevelop10() + workCriteria.getDevelop50() + workCriteria.getDevelop100()));
        List<WorkStage> workStageList = workStageService.findWorkStage(workId, 1);
        workStageList.forEach(
                workStage -> timeStage.set(timeStage.get()
                + workStage.getStage0()
                + workStage.getStage1()
                + workStage.getStage2()
                + workStage.getStage3()
                + workStage.getStage4()
        ));
        float time = timeCriteria.get() - timeStage.get();
        if (time < 0) {
            return new AttrDto(time, "Плановой оценки больше чем критериев на " + time * -1);
        }
        if (time == 0) {
            return new AttrDto(time, "");
        }
        return new AttrDto(time, "Критериев больше чем плановой оценки на " + time);
    }

    public WorkStageDto AllTime(Long workId) {
        final Float[] stage = new Float[5];
        stage[0] = 0f;
        stage[1] = 0f;
        stage[2] = 0f;
        stage[3] = 0f;
        stage[4] = 0f;
        workStageService.findWorkStage(workId).forEach(workStage -> {
            stage[0] = stage[0] + workStage.getStage0();
            stage[1] = stage[1] + workStage.getStage1();
            stage[2] = stage[2] + workStage.getStage2();
            stage[3] = stage[3] + workStage.getStage3();
            stage[4] = stage[4] + workStage.getStage4();
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
                workId);
    }
}
