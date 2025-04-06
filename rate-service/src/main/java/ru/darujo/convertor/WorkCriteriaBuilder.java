package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkCriteriaDto;
import ru.darujo.model.WorkCriteria;

public class WorkCriteriaBuilder {
    public static WorkCriteriaBuilder createWorkCriteria() {
        return new WorkCriteriaBuilder();
    }

    private Long id;
    private Integer criteria;
    private Float develop10;
    private Float develop50;
    private Float develop100;
    private Long workId;

    public WorkCriteriaBuilder setId(Long id) {
        this.id = id;
        return this;
    }


    public WorkCriteriaBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public WorkCriteriaBuilder setCriteria(Integer criteria) {
        this.criteria = criteria;
        return this;
    }

    public WorkCriteriaBuilder setDevelop10(Float develop10) {
        this.develop10 = develop10;
        return this;
    }

    public WorkCriteriaBuilder setDevelop50(Float develop50) {
        this.develop50 = develop50;
        return this;
    }

    public WorkCriteriaBuilder setDevelop100(Float develop100) {
        this.develop100 = develop100;
        return this;
    }

    public WorkCriteriaDto getWorkCriteriaDto() {
        return new WorkCriteriaDto(id, criteria, develop10, develop50, develop100, workId);
    }

    public WorkCriteria getWorkCriteria() {
        return new WorkCriteria(id, criteria, develop10, develop50, develop100, workId);
    }
}
