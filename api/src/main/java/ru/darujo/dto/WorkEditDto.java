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
    // ВЕНДЕРКА
    private Date analiseEndFact;
    // ВЕНДЕРКА План
    private Date analiseEndPlan;

    //конец разработки факт
    private Date developEndFact;
    //конец разработки план
    private Date developEndPlan;
    // конец Стабилизация прототипа факт
    private Date debugEndFact;
    // конец Стабилизация прототипа план
    private Date debugEndPlan;
    // конец Стабилизация релиза
    private Date releaseEndFact;
    // конец Стабилизация релиза
    private Date releaseEndPlan;
    // конец ОПЭ релиза
    private Date opeEndFact;
    // конец ОПЭ релиза
    private Date opeEndPlan;
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

    public WorkEditDto(Long id, Long codeSap, String codeZI, String name, Date analiseEndFact, Date analiseEndPlan, Date developEndFact, Date developEndPlan, Date debugEndFact, Date debugEndPlan, Date releaseEndFact, Date releaseEndPlan, Date opeEndFact, Date opeEndPlan, String task, String description, Date planDateStage0, Date factDateStage0, Date startTaskPlan, Date startTaskFact, Float laborDevelop, Float laborDebug, Float laborRelease, Float laborOPE, Integer stageZI, String release, Date issuingReleasePlan, Date issuingReleaseFact) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.analiseEndFact = analiseEndFact;
        this.analiseEndPlan = analiseEndPlan;
        this.developEndFact = developEndFact;
        this.developEndPlan = developEndPlan;
        this.debugEndFact = debugEndFact;
        this.debugEndPlan = debugEndPlan;
        this.releaseEndFact = releaseEndFact;
        this.releaseEndPlan = releaseEndPlan;
        this.opeEndFact = opeEndFact;
        this.opeEndPlan = opeEndPlan;
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

    public Long getId() {
        return id;
    }

    public Date getDevelopEndFact() {
        return developEndFact;
    }

    public Date getDebugEndFact() {
        return debugEndFact;
    }

    public Date getReleaseEndFact() {
        return releaseEndFact;
    }

    public Date getOpeEndFact() {
        return opeEndFact;
    }

    public Date getAnaliseEndFact() {
        return analiseEndFact;
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


    public WorkEditDto() {
    }


    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public Date getDevelopEndPlan() {
        return developEndPlan;
    }

    public Date getDebugEndPlan() {
        return debugEndPlan;
    }

    public Date getReleaseEndPlan() {
        return releaseEndPlan;
    }

    public Date getOpeEndPlan() {
        return opeEndPlan;
    }

    public Date getAnaliseEndPlan() {
        return analiseEndPlan;
    }

    public Date getFactDateStage0() {
        return factDateStage0;
    }
}
