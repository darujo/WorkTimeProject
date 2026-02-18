package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkCriteriaDto;
import ru.darujo.model.WorkCriteria;

public class WorkCriteriaConvertor {
    public static WorkCriteriaDto getWorkCriteriaDto(WorkCriteria workCriteria){
        return WorkCriteriaBuilder
                .createWorkCriteria()
                .setId(workCriteria.getId())
                .setCriteria(workCriteria.getCriteria())
                .setDevelop10(workCriteria.getDevelop10())
                .setDevelop50(workCriteria.getDevelop50())
                .setDevelop100(workCriteria.getDevelop100())
                .setWorkId(workCriteria.getWorkId())
                .setProjectId(workCriteria.getProjectId())
                .getWorkCriteriaDto();
    }
    public static WorkCriteria getWorkCriteria(WorkCriteriaDto workCriteria){
        return WorkCriteriaBuilder.createWorkCriteria()
                .setId(workCriteria.getId())
                .setCriteria(workCriteria.getCriteria())
                .setDevelop10(workCriteria.getDevelop10())
                .setDevelop50(workCriteria.getDevelop50())
                .setDevelop100(workCriteria.getDevelop100())
                .setWorkId(workCriteria.getWorkId())
                .setProjectId(workCriteria.getProjectId())
                .getWorkCriteria();    }
}
