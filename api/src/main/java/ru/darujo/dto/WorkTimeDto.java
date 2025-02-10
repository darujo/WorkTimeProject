package ru.darujo.dto;

import java.io.Serializable;
import java.util.Date;

public class WorkTimeDto implements Serializable {
    private Long id;
    private String userName;
    private Date workDate;
    private float workTime;
    private Long work_id;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public float getWorkTime() {
        return workTime;
    }

    public Long getWorkId() {
        return work_id;
    }

    public WorkTimeDto(Long id, String userName, Date workDate, float workTime, Long work_id) {
        this.id = id;
        this.userName = userName;
        this.workDate = workDate;
        this.workTime = workTime;
        this.work_id = work_id;
    }
}