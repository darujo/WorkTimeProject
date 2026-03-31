package ru.darujo.dto.ratestage;

import ru.darujo.dto.work.WorkLittleDto;

import java.io.Serializable;
import java.util.List;

public class WorkRateDto implements Serializable {
    @SuppressWarnings("unused")
    public WorkRateDto() {
    }

    private WorkLittleDto work;
    private List<RateDto> rateList;
    private WorkStageDto stageTotal;

    public WorkRateDto(WorkLittleDto work, List<RateDto> rateList, WorkStageDto stageTotal) {
        this.work = work;
        this.rateList = rateList;
        this.stageTotal = stageTotal;
    }

    @SuppressWarnings("unused")
    public WorkLittleDto getWork() {
        return work;
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
