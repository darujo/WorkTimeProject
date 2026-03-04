package ru.darujo.dto.workperiod;

import java.util.List;

public class WorkUserTime {
    @SuppressWarnings("unused")
    public WorkUserTime() {
    }

    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование Зи
    private String name;
    private List<UserWorkFormDto> userWorkFormDTOs;

    public WorkUserTime(Long id, Long codeSap, String codeZI, String name, List<UserWorkFormDto> userWorkFormDTOs) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.userWorkFormDTOs = userWorkFormDTOs;
    }

    public Long getId() {
        return id;
    }

    public Long getCodeSap() {
        return codeSap;
    }

    @SuppressWarnings("unused")
    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }

    public List<UserWorkFormDto> getUserWorkFormDTOs() {
        return userWorkFormDTOs;
    }
}
