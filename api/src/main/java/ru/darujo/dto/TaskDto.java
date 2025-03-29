package ru.darujo.dto;

import java.io.Serializable;

public class TaskDto implements Serializable, UserFio{
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
    private  Integer type;

    public String getTypeStr() {
        if(type ==1){
            return "ЗИ";
        }else if (type == 2){
            return "Вендерка";
        }else if (type== 3){
            return "Админ";
        }
        return "Не известный тип";
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
    public String getCodeZi() {
        return codeZi;
    }

    public String getNameZi() {
        return nameZi;
    }
    public TaskDto() {
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public TaskDto(Long id, String nikName, String authorFirstName,String authorLastName,String authorPatronymic, String codeBTS, String codeDEVBO, String description, Integer type, Long workId) {
        this.id = id;
        this.nikName = nikName;
        this.authorFirstName = authorFirstName;
        this.authorLastName  = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.codeBTS = codeBTS;
        this.codeDEVBO = codeDEVBO;
        this.description = description;
        this.type = type;
        this.workId = workId;
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
}
