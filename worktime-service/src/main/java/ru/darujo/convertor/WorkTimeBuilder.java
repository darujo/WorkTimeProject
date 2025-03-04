package ru.darujo.convertor;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

import java.util.Date;

public class WorkTimeBuilder {
    private Long id;
    private String userName;
    private Float  workTime;
    private Date   workDate;
    private Long   taskId;
    private String comment;

    public WorkTimeBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkTimeBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public WorkTimeBuilder setWorkTime(Float workTime) {
        this.workTime = workTime;
        return this;
    }
    public WorkTimeBuilder setWorkDate(Date workDate) {
        this.workDate = workDate;
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

    public static WorkTimeBuilder createWorkTime () {
        return new WorkTimeBuilder();
    }

    public WorkTimeDto getWorkTimeDto(){
        return new WorkTimeDto(id,userName,workDate,workTime,taskId,comment);
    }
    public WorkTime getWorkTime(){
        return new WorkTime(id,userName,workDate,workTime,taskId,comment);
    }
}
