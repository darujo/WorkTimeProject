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

    public WorkTypeDto(Long id, String type, Float time, Long workId) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.workId = workId;
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
}
