package ru.darujo.dto.work;

import ru.darujo.assistant.helper.DateHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReleaseStageDto {
    private Long id;
    // Порядковый номер релиза
    private String name;
    // Выдача релиза даты План
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;
    private Float sort;

    @SuppressWarnings("unchecked")
    private final List<WorkLittleDto>[] works = new ArrayList[7];

    public ReleaseStageDto(Long id, String name, Timestamp issuingReleasePlan, Timestamp issuingReleaseFact, Float sort) {
        this.id = id;
        this.name = name;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.sort = sort;
        for (int i = 0; i < works.length; i++) {
            works[i] = new ArrayList<>();
        }

    }

    public List<WorkLittleDto>[] getWorks() {
        return works;
    }

    @SuppressWarnings("unused")
    public ReleaseStageDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    @SuppressWarnings("unused")
    public String getIssuingReleasePlanStr() {
        return DateHelper.dateToDDMMYYYY(issuingReleasePlan);
    }

    @SuppressWarnings("unused")
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
}
