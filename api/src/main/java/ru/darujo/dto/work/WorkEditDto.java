package ru.darujo.dto.work;

import java.io.Serializable;
import java.sql.Timestamp;

public class WorkEditDto implements Serializable, WorkPlanTime {
    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Разработка прототипа
    private String name;
    // ВЕНДЕРКА
    private Timestamp analiseEndFact;
    // ВЕНДЕРКА План
    private Timestamp analiseEndPlan;

    //конец разработки факт
    private Timestamp developEndFact;
    //конец разработки план
    private Timestamp developEndPlan;
    // конец Стабилизация прототипа факт
    private Timestamp debugEndFact;
    // конец Стабилизация прототипа план
    private Timestamp debugEndPlan;
    // конец Стабилизация релиза
    private Timestamp releaseEndFact;
    // конец Стабилизация релиза
    private Timestamp releaseEndPlan;
    // конец ОПЭ релиза
    private Timestamp opeEndFact;
    // конец ОПЭ релиза
    private Timestamp opeEndPlan;
    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;

    // Дата начала доработки План
    private Timestamp startTaskPlan;
    // Дата начала доработки Факт
    private Timestamp startTaskFact;
    // Плановые трудозатраты, чел/час анализ
    private Float laborAnalise;
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
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;

    public WorkEditDto(Long id,
                       Long codeSap,
                       String codeZI,
                       String name,
                       Timestamp analiseEndFact,
                       Timestamp analiseEndPlan,
                       Timestamp developEndFact,
                       Timestamp developEndPlan,
                       Timestamp debugEndFact,
                       Timestamp debugEndPlan,
                       Timestamp releaseEndFact,
                       Timestamp releaseEndPlan,
                       Timestamp opeEndFact,
                       Timestamp opeEndPlan,
                       String task,
                       String description,
                       Timestamp startTaskPlan,
                       Timestamp startTaskFact,
                       Integer stageZI,
                       String release,
                       Timestamp issuingReleasePlan,
                       Timestamp issuingReleaseFact) {
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
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.stageZI = stageZI;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Timestamp getDevelopEndFact() {
        return developEndFact;
    }

    public Timestamp getDebugEndFact() {
        return debugEndFact;
    }

    public Timestamp getReleaseEndFact() {
        return releaseEndFact;
    }

    public Timestamp getOpeEndFact() {
        return opeEndFact;
    }

    public Timestamp getAnaliseEndFact() {
        return analiseEndFact;
    }

    public String getName() {return name; }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getStartTaskPlan() {
        return startTaskPlan;
    }

    public Timestamp getStartTaskFact() {
        return startTaskFact;
    }

    public Float getLaborAnalise() {
        return laborAnalise;
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

    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    public Timestamp getIssuingReleaseFact() {
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

    public Timestamp getDevelopEndPlan() {
        return developEndPlan;
    }

    public Timestamp getDebugEndPlan() {
        return debugEndPlan;
    }

    public Timestamp getReleaseEndPlan() {
        return releaseEndPlan;
    }

    public Timestamp getOpeEndPlan() {
        return opeEndPlan;
    }

    public Timestamp getAnaliseEndPlan() {
        return analiseEndPlan;
    }

    @Override
    public void setLaborAnalise(Float laborAnalise) {
        this.laborAnalise = laborAnalise;
    }

    @Override
    public void setLaborDevelop(Float laborDevelop) {
        this.laborDevelop = laborDevelop;
    }

    @Override
    public void setLaborDebug(Float laborDebug) {
        this.laborDebug = laborDebug;
    }

    @Override
    public void setLaborRelease(Float laborRelease) {
        this.laborRelease = laborRelease;
    }

    @Override
    public void setLaborOPE(Float laborOPE) {
        this.laborOPE = laborOPE;
    }
}
