package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkTypeDto;
import ru.darujo.model.WorkType;

public class WorkTypeBuilder {
    public static WorkTypeBuilder createWorkType() {
        return new WorkTypeBuilder();
    }

    private Long id;
    private String type;
    private Float time;
    private Long workId;
    private Integer number;

    public WorkTypeBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkTypeBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public WorkTypeBuilder setTime(Float time) {
        this.time = time;
        return this;
    }

    public WorkTypeBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public WorkTypeBuilder setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public WorkTypeDto getWorkTypeDto() {
        return new WorkTypeDto(id, type, time, workId, number);
    }

    public WorkType getWorkType() {
        return new WorkType(id, type, time, workId, number);
    }
}
