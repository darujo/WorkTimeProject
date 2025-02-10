package ru.darujo.dto;

import java.util.Date;

public class WorkDto {
    private Long id;
    // Разработка прототипа
    private Date dateStartDevelop;
    // Стабилизация прототипа
    private Date dateStartDebug;
    // Стабилизация релиза
    private Date dateStartRelease;
    // ОПЭ релиза
    private Date dateStartOPE;
    // ВЕНДЕРКА
    private Date dateStartWender;

    public Long getId() {
        return id;
    }

    public Date getDateStartDevelop() {
        return dateStartDevelop;
    }

    public Date getDateStartDebug() {
        return dateStartDebug;
    }

    public Date getDateStartRelease() {
        return dateStartRelease;
    }

    public Date getDateStartOPE() {
        return dateStartOPE;
    }

    public Date getDateStartWender() {
        return dateStartWender;
    }

    public WorkDto() {
    }

    public WorkDto(Long id, Date dateStartDevelop, Date dateStartDebug, Date dateStartRelease, Date dateStartOPE, Date dateStartWender) {
        this.id = id;
        this.dateStartDevelop = dateStartDevelop;
        this.dateStartDebug = dateStartDebug;
        this.dateStartRelease = dateStartRelease;
        this.dateStartOPE = dateStartOPE;
        this.dateStartWender = dateStartWender;
    }
}
