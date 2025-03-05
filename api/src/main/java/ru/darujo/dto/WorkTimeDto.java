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
    private String nikName;
    private Date workDate;
    private String workDateStr;
    private String comment;

    private Float workTime;
    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public WorkTimeDto() {
    }

    public WorkTimeDto(Long id, String nikName, Date workDate, Float workTime, Long workId, String comment) {
        this.id = id;
        this.nikName = nikName;
        this.workDate = workDate;
        this.workTime = workTime;
        this.taskId = workId;
        this.comment = comment;
    }

    public String getNikName() {
        return nikName;
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