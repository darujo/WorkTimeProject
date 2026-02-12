package ru.darujo.dto.work;

import ru.darujo.assistant.helper.DateHelper;

import java.sql.Timestamp;

public class ReleaseEditDto {
    private Long id;
    // Порядковый номер релиза
    private String name;
    private Long projectId;
    // Выдача релиза даты План
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;
    private Float sort;

    public ReleaseEditDto(Long id, String name, Long projectId, Timestamp issuingReleasePlan, Timestamp issuingReleaseFact, Float sort) {
        this.id = id;
        this.name = name;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.sort = sort;
        this.projectId = projectId;
    }

    @SuppressWarnings("unused")
    public ReleaseEditDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    @SuppressWarnings("unused")
    public String getIssuingReleasePlanStr() {
        return DateHelper.dateToDDMMYYYY(issuingReleasePlan);
    }

    public Timestamp getIssuingReleaseFact() {
        return issuingReleaseFact;
    }

    @SuppressWarnings("unused")
    public String getIssuingReleaseFactStr() {
        return DateHelper.dateToDDMMYYYY(issuingReleaseFact);
    }

    @SuppressWarnings("unused")
    public Float getSort() {
        return sort;
    }

    @SuppressWarnings("unused")
    public Long getProjectId() {
        return projectId;
    }

    public void setSort(Float sort) {
        this.sort = sort;
    }
}
