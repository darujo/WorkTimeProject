package ru.darujo.dto;

import ru.darujo.dto.user.UserFio;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TaskDto implements Serializable, UserFio {
    private Long id;
    private String nikName;

    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;

    // № запроса (BTS)
    private String codeBTS;
    // № внутренней задачи (D E V B O)
    private String codeDEVBO;
    // Краткое описание ошибки
    private String description;
    // Тип задачи
    private Integer type;
    private String typeStr;
    private String executor;
    private String analyst;
    private String developer;
    private String tester;
    private String status;

    @SuppressWarnings("unused")
    public String getTypeStr() {
        return typeStr;
    }

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
    private ZonedDateTime timeCreate;

    public void setNikName(String nikName) {
        this.nikName = nikName;
    }

    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
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

    @SuppressWarnings("unused")
    public String getCodeZi() {
        return codeZi;
    }

    @SuppressWarnings("unused")
    public String getNameZi() {
        return nameZi;
    }

    @SuppressWarnings("unused")
    public TaskDto() {
    }

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

    public TaskDto(Long id,
                   String nikName,
                   String authorFirstName,
                   String authorLastName,
                   String authorPatronymic,
                   String codeBTS,
                   String codeDEVBO,
                   String description,
                   Integer type,
                   String typeStr,
                   Long workId,
                   ZonedDateTime timeCreate,
                   String executor,
                   String analyst,
                   String developer,
                   String tester,
                   String status) {
        this.id = id;
        this.nikName = nikName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.codeBTS = codeBTS;
        this.codeDEVBO = codeDEVBO;
        this.description = description;
        this.type = type;
        this.typeStr = typeStr;
        this.workId = workId;
        this.timeCreate = timeCreate;
        this.executor = executor;
        this.analyst = analyst;
        this.developer = developer;
        this.tester = tester;
        this.status = status;
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

    public ZonedDateTime getTimeCreate() {
        return timeCreate;
    }

    public String getExecutor() {
        return executor;
    }

    public String getAnalyst() {
        return analyst;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getTester() {
        return tester;
    }

    public String getStatus() {
        return status;
    }
}
