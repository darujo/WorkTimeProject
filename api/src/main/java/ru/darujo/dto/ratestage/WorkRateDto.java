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
    private List<RateDto> rateDtoList;
    private WorkStageDto workStageDto;

    public WorkRateDto(String name, Long sap, String code, List<RateDto> rateDtoList, WorkStageDto workStageDto) {
        this.name = name;
        this.sap = sap;
        this.code = code;
        this.rateDtoList = rateDtoList;
        this.workStageDto = workStageDto;
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
    public List<RateDto> getRateDtoList() {
        return rateDtoList;
    }

    @SuppressWarnings("unused")
    public WorkStageDto getWorkStageDto() {
        return workStageDto;
    }
}
