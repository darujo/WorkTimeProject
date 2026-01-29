package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

import java.sql.Timestamp;

public class WorkTimeBuilder {
    private Long id;
    private String nikName;
    private Float workTime;
    private Timestamp workDate;
    private Long taskId;
    private String comment;
    private Integer type;
    private Long projectId;

    public WorkTimeBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkTimeBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public WorkTimeBuilder setWorkTime(Float workTime) {
        this.workTime = workTime;
        return this;
    }

    public WorkTimeBuilder setWorkDate(Timestamp workDate) {
        if (workDate == null) {
            this.workDate = null;
        } else {
            this.workDate = DateHelper.dateNoTime(workDate);
        }
        return this;
    }

    public WorkTimeBuilder setTaskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public WorkTimeBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public WorkTimeBuilder setType(Integer type) {
        this.type = type;
        return this;
    }

    public WorkTimeBuilder setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public static WorkTimeBuilder createWorkTime() {
        return new WorkTimeBuilder();
    }


    public WorkTimeDto getWorkTimeDto() {
        return new WorkTimeDto(id, nikName, workDate, workTime, taskId, comment, type, projectId);
    }

    public WorkTime getWorkTime() {
        return new WorkTime(id, nikName, workDate, workTime, taskId, comment, type, projectId);
    }


}
