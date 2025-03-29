package ru.darujo.dto.work;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkDto implements Serializable {

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
    private Date developEndFact;
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

    public String getAnaliseEndPlan() {
        return dateToText(analiseEndPlan);
    }

    public WorkDto(Long id, Long codeSap, String codeZI, String name, Date analiseEndPlan, Date analiseEndFact, Date developEndFact, Date debugEndFact, Date releaseEndFact, Date opeEndFact, String task, String description, Date startTaskPlan, Date startTaskFact, Float laborDevelop, Float laborDebug, Float laborRelease, Float laborOPE, Integer stageZI, String release, Date issuingReleasePlan, Date issuingReleaseFact) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.analiseEndPlan = analiseEndPlan;
        this.analiseEndFact = analiseEndFact;
        this.developEndFact = developEndFact;
        this.debugEndFact = debugEndFact;
        this.releaseEndFact = releaseEndFact;
        this.opeEndFact = opeEndFact;
        this.task = task;
        this.description = description;
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
    public String getName() {return name; }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
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

    public String getDevelopEndFact() {
        return dateToText(developEndFact);
    }

    public String getDebugEndFact() {
        return dateToText(debugEndFact);
    }

    public String getReleaseEndFact() {
        return dateToText(releaseEndFact);
    }

    public String getOpeEndFact() {
        return dateToText(opeEndFact);
    }

    public String getAnaliseEndFact() {
        return dateToText(analiseEndFact);
    }

    public String getStartTaskPlan() {
        return dateToText(startTaskPlan);
    }

    public String getStartTaskFact() {
        return dateToText(startTaskFact);
    }

    public String getIssuingReleasePlan() {
        return dateToText(issuingReleasePlan);
    }

    public String getIssuingReleaseFact() {
        return dateToText(issuingReleaseFact);
    }
    public WorkDto() {
    }

    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
}
