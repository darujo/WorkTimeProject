package ru.darujo.dto;

import java.io.Serializable;
import java.util.Date;

public class WorkTimeDto implements Serializable {
    private Long id;
    private String userName;
    private Date workDate;
    private float workTime;
    private Long workId;

    public Long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public WorkTimeDto() {
    }

    public WorkTimeDto(Long id, String userName, Date workDate, float workTime, Long workId) {
        this.id = id;
        this.userName = userName;
        this.workDate = workDate;
        this.workTime = workTime;
        this.workId = workId;
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
        return workId;
    }

//    public WorkTimeDto(Long id, String userName, Date workDate, Float workTime, Long workId) {
//        this.id = id;
//        this.userName = userName;
//        this.workDate = workDate;
//        this.workTime = workTime;
//        this.workId = workId;
//    }

//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}