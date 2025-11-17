package ru.darujo.dto.ratestage;

import java.io.Serializable;

public class WorkTypeDto implements Serializable {
    @SuppressWarnings("unused")
    public WorkTypeDto() {
    }

    private Long id;
    private String type;
    private Float time;
    private Long workId;
    private Integer number;

    public WorkTypeDto(Long id, String type, Float time, Long workId, Integer number) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.workId = workId;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Float getTime() {
        return time;
    }

    public Long getWorkId() {
        return workId;
    }

    @SuppressWarnings("unused")
    public Integer getNumber() {
        return number;
    }
}
