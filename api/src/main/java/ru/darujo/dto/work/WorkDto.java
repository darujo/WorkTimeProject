package ru.darujo.dto.work;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkDto implements Serializable, WorkPlanTime {

    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Разработка прототипа
    private String name;
    // анализ
    private Date analiseEndPlan;
    private Date analiseEndFact;
    private Date issuePrototypeFact;
    // Стабилизация прототипа
    private Date debugEndFact;
    // Стабилизация релиза
    private Date releaseEndFact;
    // ОПЭ релиза
    private Date opeEndFact;
    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;
    // Дата начала доработки План
    private Date startTaskPlan;
    // Дата начала доработки Факт
    private Date startTaskFact;
    // Плановые трудозатраты, чел/час Анализ
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
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;

    @SuppressWarnings("unused")
    public String getAnaliseEndPlan() {
        return dateToText(analiseEndPlan);
    }

    public WorkDto(Long id,
                   Long codeSap,
                   String codeZI,
                   String name,
                   Date analiseEndPlan,
                   Date analiseEndFact,
                   Date developEndFact,
                   Date debugEndFact,
                   Date releaseEndFact,
                   Date opeEndFact,
                   String task,
                   String description,
                   Date startTaskPlan,
                   Date startTaskFact,
                   Integer stageZI,
                   String release,
                   Date issuingReleasePlan,
                   Date issuingReleaseFact) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.analiseEndPlan = analiseEndPlan;
        this.analiseEndFact = analiseEndFact;
        this.issuePrototypeFact = developEndFact;
        this.debugEndFact = debugEndFact;
        this.releaseEndFact = releaseEndFact;
        this.opeEndFact = opeEndFact;
        this.task = task;
        this.description = description;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.stageZI = stageZI;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
    }

    public Long getId() {
        return id;
    }
    public String getName() {return name; }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    @SuppressWarnings("unused")
    public Float getLaborDevelop() {
        return laborDevelop;
    }

    @SuppressWarnings("unused")
    public Float getLaborDebug() {
        return laborDebug;
    }

    @SuppressWarnings("unused")
    public Float getLaborRelease() {
        return laborRelease;
    }

    @SuppressWarnings("unused")
    public Float getLaborOPE() {
        return laborOPE;
    }

    @SuppressWarnings("unused")
    public Integer getStageZI() {
        return stageZI;
    }

    public String getRelease() {
        return release;
    }

    @SuppressWarnings("unused")
    public String getIssuePrototypeFact() {
        return dateToText(issuePrototypeFact);
    }

    @SuppressWarnings("unused")
    public String getDebugEndFact() {
        return dateToText(debugEndFact);
    }

    @SuppressWarnings("unused")
    public String getReleaseEndFact() {
        return dateToText(releaseEndFact);
    }

    @SuppressWarnings("unused")
    public String getOpeEndFact() {
        return dateToText(opeEndFact);
    }

    @SuppressWarnings("unused")
    public String getAnaliseEndFact() {
        return dateToText(analiseEndFact);
    }

    @SuppressWarnings("unused")
    public String getStartTaskPlan() {
        return dateToText(startTaskPlan);
    }

    @SuppressWarnings("unused")
    public String getStartTaskFact() {
        return dateToText(startTaskFact);
    }

    @SuppressWarnings("unused")
    public String getIssuingReleasePlan() {
        return dateToText(issuingReleasePlan);
    }

    @SuppressWarnings("unused")
    public String getIssuingReleaseFact() {
        return dateToText(issuingReleaseFact);
    }
    public WorkDto() {
    }

    public Long getCodeSap() {
        return codeSap;
    }

    @SuppressWarnings("unused")
    public String getCodeZI() {
        return codeZI;
    }

    @SuppressWarnings("unused")
    public Float getLaborAnalise() {
        return laborAnalise;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }

    @Override
    public void setLaborAnalise(Float laborAnalise) {
        this.laborAnalise = laborAnalise;
    }

    public void setLaborDevelop(Float laborDevelop) {
        this.laborDevelop = laborDevelop;
    }

    public void setLaborDebug(Float laborDebug) {
        this.laborDebug = laborDebug;
    }

    public void setLaborRelease(Float laborRelease) {
        this.laborRelease = laborRelease;
    }

    public void setLaborOPE(Float laborOPE) {
        this.laborOPE = laborOPE;
    }
}
