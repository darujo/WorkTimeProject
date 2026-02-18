package ru.darujo.dto;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.user.UserFio;

import java.io.Serializable;
import java.sql.Timestamp;

public class WorkTimeDto extends DateHelper implements Serializable, UserFio {

    private Long id;
    private String nikName;
    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;

    @SuppressWarnings("unused")
    public String getAuthorFirstName() {
        return authorFirstName;
    }

    @SuppressWarnings("unused")
    public String getAuthorLastName() {
        return authorLastName;
    }

    @SuppressWarnings("unused")
    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public void setFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public void setPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    private Timestamp workDate;
    @SuppressWarnings("unused")
    private String workDateStr;
    private String comment;

    private Float workTime;
    private Long taskId;
    private Integer type;
    @SuppressWarnings("unused")
    private String typeStr;
    private String adminStr;
    private String taskDescription;
    private String taskCodeBTS;
    // № внутренней задачи (DEVBO)
    private String taskCodeDEVBO;
    private Integer taskType;
    private Long projectId;

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskCodeBTS(String taskCodeBTS) {
        this.taskCodeBTS = taskCodeBTS;
    }

    public void setTaskCodeDEVBO(String taskCodeDEVBO) {
        this.taskCodeDEVBO = taskCodeDEVBO;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    @SuppressWarnings("unused")
    public String getTaskDescription() {
        return taskDescription;
    }

    @SuppressWarnings("unused")
    public String getTaskCodeBTS() {
        return taskCodeBTS;
    }

    @SuppressWarnings("unused")
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

    public WorkTimeDto(Long id, String nikName, Timestamp workDate, Float workTime, Long taskId, String comment, Integer type, String typeStr, String adminStr, Long projectId) {
        this.id = id;
        this.nikName = nikName;
        this.workDate = workDate;
        this.workTime = workTime;
        this.taskId = taskId;
        this.comment = comment;
        this.type = type;
        this.typeStr = typeStr;
        this.adminStr = adminStr;
        this.projectId = projectId;
    }

    public String getNikName() {
        return nikName;
    }

    @SuppressWarnings("unused")
    public String getTypeStr() {
        if (type == null) {
            return null;
        } else if (taskType != null && taskType == 3) {
            return adminStr;
        } else {
            return typeStr;
        }
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

    @SuppressWarnings("unused")
    public String getWorkDateStr() {
        return dateToDDMMYYYY(workDate);
    }

    public Integer getType() {
        return type;
    }

    public void setWorkTime(Float workTime) {
        this.workTime = workTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}