package ru.darujo.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkTimeDto  implements Serializable, UserFio {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
    private Long id;
    private String nikName;
    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public void setAuthorPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    private Timestamp workDate;
    private String workDateStr;
    private String comment;

    private Float workTime;
    private Long taskId;
    private String taskDescription;
    private String taskCodeBTS;
    // № внутренней задачи (DEVBO)
    private String taskCodeDEVBO;

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskCodeBTS(String taskCodeBTS) {
        this.taskCodeBTS = taskCodeBTS;
    }

    public void setTaskCodeDEVBO(String taskCodeDEVBO) {
        this.taskCodeDEVBO = taskCodeDEVBO;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskCodeBTS() {
        return taskCodeBTS;
    }

    public String getTaskCodeDEVBO() {
        return taskCodeDEVBO;
    }

    public Long getId() {
        return id;
    }

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public WorkTimeDto() {
    }

    public WorkTimeDto(Long id, String nikName, Timestamp workDate, Float workTime, Long workId, String comment) {
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

    public Timestamp getWorkDate() {
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