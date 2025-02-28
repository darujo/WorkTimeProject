package ru.darujo.dto;

import java.io.Serializable;

public class TaskDto implements Serializable {
    private Long id;
    private String userName;
    // № запроса (BTS)
    private String codeBTS;
    // № внутренней задачи (DEVBO)
    private String codeDEVBO;
    // Краткое описание ошибки
    private String description;
    // Тип задачи
    private  Integer type;
    // № ЗИ (ZI)
    private Long workId;

    public void setCodeZi(String codeZi) {
        this.codeZi = codeZi;
    }

    public void setNameZi(String nameZi) {
        this.nameZi = nameZi;
    }

    private String codeZi;
    private String nameZi;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getCodeBTS() {
        return codeBTS;
    }

    public String getCodeDEVBO() {
        return codeDEVBO;
    }

    public String getDescription() {
        return description;
    }

    public Integer getType() {
        return type;
    }

    public Long getWorkId() {
        return workId;
    }
    public String getCodeZi() {
        return codeZi;
    }

    public String getNameZi() {
        return nameZi;
    }
    public TaskDto() {
    }

    public TaskDto(Long id, String userName, String codeBTS, String codeDEVBO, String description, Integer type, Long workId) {
        this.id = id;
        this.userName = userName;
        this.codeBTS = codeBTS;
        this.codeDEVBO = codeDEVBO;
        this.description = description;
        this.type = type;
        this.workId = workId;
    }



}
