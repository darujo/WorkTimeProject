package ru.darujo.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkTimeDto implements Serializable {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
    private Long id;
    private String userName;
    private Date workDate;
    private String workDateStr;
    private String comment;

    private Float workTime;
    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public WorkTimeDto() {
    }

    public WorkTimeDto(Long id, String userName, Date workDate, Float workTime, Long workId, String comment) {
        this.id = id;
        this.userName = userName;
        this.workDate = workDate;
        this.workTime = workTime;
        this.taskId = workId;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public Float getWorkTime() {
        return workTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getComment() {
        return comment;
    }

    public String getWorkDateStr() {
        return dateToText(workDate);
    }
}