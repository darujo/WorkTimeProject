package ru.darujo.dto;

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

    public WorkLittleDto(Long id, Long codeSap, String codeZI, String name, Integer stageZI) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.stageZI = stageZI;
    }

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
