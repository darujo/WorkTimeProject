package ru.darujo.dto.workperiod;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class WorkUserTimeList implements GetList<WorkUserTime>, Serializable {
    @SuppressWarnings("unused")
    public WorkUserTimeList() {
    }

    @JsonProperty("workUserTimeList")
    @SuppressWarnings("unused")
    private List<WorkUserTime> workUserTimeList;

    public List<WorkUserTime> getList() {
        return workUserTimeList;
    }


}
