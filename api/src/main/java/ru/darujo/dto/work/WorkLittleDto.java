package ru.darujo.dto.work;

import java.io.Serializable;

public class WorkLittleDto implements Serializable {
    public WorkLittleDto() {
    }

    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование Зи
    private String name;
    private Integer stageZI;
    private Boolean rated;

    public WorkLittleDto(Long id, Long codeSap, String codeZI, String name, Integer stageZI, Boolean rated) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.stageZI = stageZI;
        this.rated = rated;
    }

    @SuppressWarnings("unused")
    public Boolean getRated() {
        return rated;
    }

    @SuppressWarnings("unused")
    public Integer getStageZI() {
        return stageZI;
    }

    public Long getId() {
        return id;
    }

    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }
}
