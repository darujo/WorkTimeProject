package ru.darujo.dto.content;

import java.util.List;

public class WorkContent extends ContentObject {
    @SuppressWarnings("unused")
    public WorkContent() {
    }

    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование Зи
    private String name;

    public WorkContent(Long id, Long codeSap, String codeZI, String name, List<? extends ContentElement> content) {
        super(content);
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;

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
}
