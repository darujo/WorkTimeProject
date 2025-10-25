package ru.darujo.dto;

import ru.darujo.dto.user.UserFio;
import ru.darujo.service.CodeService;

import java.io.Serializable;
import java.sql.Timestamp;

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

    @SuppressWarnings("unused")
    public String getTypeStr() {
        return CodeService.getTaskType(type);
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
    private Timestamp timeCreate;

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
                   Long workId,
                   Timestamp timeCreate) {
        this.id = id;
        this.nikName = nikName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.codeBTS = codeBTS;
        this.codeDEVBO = codeDEVBO;
        this.description = description;
        this.type = type;
        this.workId = workId;
        this.timeCreate = timeCreate;
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

    public Timestamp getTimeCreate() {
        return timeCreate;
    }
}
