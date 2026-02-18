package ru.darujo.dto.ratestage;

import java.io.Serializable;
import java.util.List;

public class WorkRateDto implements Serializable {
    @SuppressWarnings("unused")
    public WorkRateDto() {
    }

    private String name;
    private Long sap;
    private String code;
    private List<RateDto> rateList;
    private WorkStageDto stageTotal;

    public WorkRateDto(String name, Long sap, String code, List<RateDto> rateList, WorkStageDto stageTotal) {
        this.name = name;
        this.sap = sap;
        this.code = code;
        this.rateList = rateList;
        this.stageTotal = stageTotal;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public Long getSap() {
        return sap;
    }

    @SuppressWarnings("unused")
    public String getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public List<RateDto> getRateList() {
        return rateList;
    }

    @SuppressWarnings("unused")
    public WorkStageDto getStageTotal() {
        return stageTotal;
    }
}
