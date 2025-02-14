package ru.darujo.convertor;

import ru.darujo.dto.WorkTimeDto;
import ru.darujo.model.WorkTime;

import java.util.Date;

public class WorkTimeBuilder {
    private Long id;
    private String userName;
    private float  workTime;
    private Date   workDate;
    private Long   workId;

    public WorkTimeBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkTimeBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public WorkTimeBuilder setWorkTime(float workTime) {
        this.workTime = workTime;
        return this;
    }
    public WorkTimeBuilder setWorkDate(Date workDate) {
        this.workDate = workDate;
        return this;
    }
    public WorkTimeBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }
    public static WorkTimeBuilder createWorkTime () {
        return new WorkTimeBuilder();
    }
    public WorkTimeDto getWorkTimeDto(){
        return new WorkTimeDto(id,userName,workDate,workTime,workId);
    }
    public WorkTime getWorkTime(){
        return new WorkTime(id,userName,workDate,workTime,workId);
    }
}
