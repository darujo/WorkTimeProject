package ru.darujo.dto;

import java.io.Serializable;
import java.util.Date;

public class WorkEditDto implements Serializable {
    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Разработка прототипа
    private String name;
    //начало разработки факт
    private Date dateStartDevelop;
    //начало разработки план
    private Date dateStartDevelopPlan;
    // Стабилизация прототипа факт
    private Date dateStartDebug;
    // Стабилизация прототипа план
    private Date dateStartDebugPlan;
    // Стабилизация релиза
    private Date dateStartRelease;
    // Стабилизация релиза
    private Date dateStartReleasePlan;
    // ОПЭ релиза
    private Date dateStartOPE;
    // ОПЭ релиза
    private Date dateStartOPEPlan;
    // ВЕНДЕРКА
    private Date dateStartWender;
    // ВЕНДЕРКА План
    private Date dateStartWenderPlan;
    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;
    // Плановая дата завершения 0 этапа
    private Date planDateStage0;
    // Плановая дата завершения 0 этапа
    private Date factDateStage0;

    // Дата начала доработки План
    private Date startTaskPlan;
    // Дата начала доработки Факт
    private Date startTaskFact;
    // Плановые трудозатраты, чел/час Разработка прототипа
    private Float laborDevelop;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // Плановые трудозатраты, чел/час ОПЭ
    private Float laborOPE;
    // Текущий этап ЗИ
    private Integer stageZI;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;


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

    public String getName() {return name; }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    public Date getPlanDateStage0() {
        return planDateStage0;
    }

    public Date getStartTaskPlan() {
        return startTaskPlan;
    }

    public Date getStartTaskFact() {
        return startTaskFact;
    }

    public Float getLaborDevelop() {
        return laborDevelop;
    }

    public Float getLaborDebug() {
        return laborDebug;
    }

    public Float getLaborRelease() {
        return laborRelease;
    }

    public Float getLaborOPE() {
        return laborOPE;
    }

    public Integer getStageZI() {
        return stageZI;
    }

    public String getRelease() {
        return release;
    }

    public Date getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    public Date getIssuingReleaseFact() {
        return issuingReleaseFact;
    }

    public WorkEditDto(Long id,Long codeSap,String codeZI, String name, Date dateStartDevelop, Date dateStartDevelopPlan, Date dateStartDebug, Date dateStartDebugPlan, Date dateStartRelease, Date dateStartReleasePlan, Date dateStartOPE, Date dateStartOPEPlan, Date dateStartWender, Date dateStartWenderPlan, String task, String description, Date planDateStage0, Date factDateStage0, Date startTaskPlan, Date startTaskFact, Float laborDevelop, Float laborDebug, Float laborRelease, Float laborOPE, Integer stageZI, String release, Date issuingReleasePlan, Date issuingReleaseFact) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI =codeZI;
        this.name = name;
        this.dateStartDevelop = dateStartDevelop;
        this.dateStartDebug = dateStartDebug;
        this.dateStartRelease = dateStartRelease;
        this.dateStartOPE = dateStartOPE;
        this.dateStartWender = dateStartWender;
        this.dateStartDevelopPlan = dateStartDevelopPlan;
        this.dateStartDebugPlan = dateStartDebugPlan;
        this.dateStartReleasePlan = dateStartReleasePlan;
        this.dateStartOPEPlan = dateStartOPEPlan;
        this.dateStartWenderPlan = dateStartWenderPlan;

        this.task = task;
        this.description = description;
        this.planDateStage0 = planDateStage0;
        this.factDateStage0 = factDateStage0;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.laborDevelop = laborDevelop;
        this.laborDebug = laborDebug;
        this.laborRelease = laborRelease;
         this.laborOPE = laborOPE;
        this.stageZI = stageZI;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
    }

    public WorkEditDto() {
    }


    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public Date getDateStartDevelopPlan() {
        return dateStartDevelopPlan;
    }

    public Date getDateStartDebugPlan() {
        return dateStartDebugPlan;
    }

    public Date getDateStartReleasePlan() {
        return dateStartReleasePlan;
    }

    public Date getDateStartOPEPlan() {
        return dateStartOPEPlan;
    }

    public Date getDateStartWenderPlan() {
        return dateStartWenderPlan;
    }

    public Date getFactDateStage0() {
        return factDateStage0;
    }
}
