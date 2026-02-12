package ru.darujo.dto.workrep;

import java.io.Serializable;
import java.util.List;

public class WorkRepDto implements Serializable {
    @SuppressWarnings("unused")
    public WorkRepDto() {
    }

    private Long id;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    private List<WorkRepProjectDto> workRepProjectDtoList;

    public WorkRepDto(Long id, String codeZI, String name, List<WorkRepProjectDto> workRepProjectDtoList) {
        this.id = id;
        this.codeZI = codeZI;
        this.name = name;
        this.workRepProjectDtoList = workRepProjectDtoList;
    }

    public Long getId() {
        return id;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public List<WorkRepProjectDto> getWorkRepProjectDtoList() {
        return workRepProjectDtoList;
    }
}
