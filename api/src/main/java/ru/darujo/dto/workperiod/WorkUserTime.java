package ru.darujo.dto.workperiod;

import java.util.List;

public class WorkUserTime {
    private final Long id;
    // Код SAP
    private final Long codeSap;
    // Код Зи
    private final String codeZI;
    // Наименование Зи
    private final String name;
    private final Integer stageZI;
    private final List<UserWorkFormDto> userWorkFormDTOs;

    public WorkUserTime(Long id, Long codeSap, String codeZI, String name, Integer stageZI, List<UserWorkFormDto> userWorkFormDTOs) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.stageZI = stageZI;
        this.userWorkFormDTOs = userWorkFormDTOs;
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

    public Integer getStageZI() {
        return stageZI;
    }

    public List<UserWorkFormDto> getUserWorkFormDTOs() {
        return userWorkFormDTOs;
    }
}
