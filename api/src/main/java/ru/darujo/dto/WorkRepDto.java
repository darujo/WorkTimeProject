package ru.darujo.dto;

import java.io.Serializable;
import java.util.Date;

public class WorkRepDto implements Serializable {
    // Код SAP
    private Integer codeSap;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Плановая дата завершения 0 этапа
    private Date planDateStage0;
    // Дата начала доработки План
    private Date startTaskPlan;
    // Дата начала доработки Факт
    private Date startTaskFact;
    private Float laborDevelop;






    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // Плановые трудозатраты, чел/час ОПЭ
    private Float laborOPE;
    // Разработка прототипа
    private Float timeAnalise;
    // Разработка прототипа
    private Float timeDevelop;
    // Стабилизация прототипа
    private Float timeDebug;
    // Стабилизация релиза
    private Float timeRelease;
    // ОПЭ релиза
    private Float timeOPE;
    // ВЕНДЕРКА
    private Float timeWender;
    // Текущий этап ЗИ
    private Integer stageZI;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;

    public Integer getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }

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

    public Float getTimeAnalise() {
        return timeAnalise;
    }

    public Float getTimeDevelop() {
        return timeDevelop;
    }

    public Float getTimeDebug() {
        return timeDebug;
    }

    public Float getTimeRelease() {
        return timeRelease;
    }

    public Float getTimeOPE() {
        return timeOPE;
    }

    public Float getTimeWender() {
        return timeWender;
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

    public WorkRepDto(Integer codeSap, String codeZI, String name, String task, String description, Date planDateStage0, Date startTaskPlan, Date startTaskFact, Float laborDevelop, Float laborDebug, Float laborRelease, Float laborOPE, Float timeAnalise, Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender, Integer stageZI, String release, Date issuingReleasePlan, Date issuingReleaseFact) {
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.task = task;
        this.description = description;
        this.planDateStage0 = planDateStage0;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.laborDevelop = laborDevelop;
        this.laborDebug = laborDebug;
        this.laborRelease = laborRelease;
        this.laborOPE = laborOPE;
        this.timeAnalise = timeAnalise;
        this.timeDevelop = timeDevelop;
        this.timeDebug = timeDebug;
        this.timeRelease = timeRelease;
        this.timeOPE = timeOPE;
        this.timeWender = timeWender;
        this.stageZI = stageZI;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
    }

    public WorkRepDto() {
    }


}
