package ru.darujo.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkDto implements Serializable {
    final private Long id;
    // Код SAP
    final private Long codeSap;
    // Код Зи
    final private String codeZI;
    // Разработка прототипа
    final private String name;
    final private Date dateStartDevelop;
    // Стабилизация прототипа
    final private Date dateStartDebug;
    // Стабилизация релиза
    final private Date dateStartRelease;
    // ОПЭ релиза
    final private Date dateStartOPE;
    // ВЕНДЕРКА
    final private Date dateStartWender;
    // № внутренней задачи (DEVBO)
    final private String task;
    // Краткое описание внутренней задачи
    final private String description;
    // Плановая дата завершения 0 этапа
    final private Date planDateStage0;
    // Дата начала доработки План
    final private Date startTaskPlan;
    // Дата начала доработки Факт
    final private Date startTaskFact;
    // Плановые трудозатраты, чел/час Разработка прототипа
    final private Float laborDevelop;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    final private Float laborDebug;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    final private Float laborRelease;
    // Плановые трудозатраты, чел/час ОПЭ
    final private Float laborOPE;
    // Текущий этап ЗИ
    final private Integer stageZI;
    // Порядковый номер релиза
    final private String release;
    // Выдача релиза даты План
    final private Date issuingReleasePlan;
    // Выдача релиза дата факт
    final private Date issuingReleaseFact;


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

    public String getPlanDateStage0() {
        return dateToText(planDateStage0);
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

    public String getDateStartDevelop() {
        return dateToText(dateStartDevelop);
    }

    public String getDateStartDebug() {
        return dateToText(dateStartDebug);
    }

    public String getDateStartRelease() {
        return dateToText(dateStartRelease);
    }

    public String getDateStartOPE() {
        return dateToText(dateStartOPE);
    }

    public String getDateStartWender() {
        return dateToText(dateStartWender);
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

    public WorkDto(Long id, Long codeSap, String codeZI, String name, Date dateStartDevelop, Date dateStartDebug, Date dateStartRelease, Date dateStartOPE, Date dateStartWender, String task, String description, Date planDateStage0, Date startTaskPlan, Date startTaskFact, Float laborDevelop, Float laborDebug, Float laborRelease, Float laborOPE, Integer stageZI, String release, Date issuingReleasePlan, Date issuingReleaseFact) {
        this.id = id;
        this.codeSap = codeSap;
        this.codeZI =codeZI;
        this.name = name;
        this.dateStartDevelop = dateStartDevelop;
        this.dateStartDebug = dateStartDebug;
        this.dateStartRelease = dateStartRelease;
        this.dateStartOPE = dateStartOPE;
        this.dateStartWender = dateStartWender;
        this.task = task;
        this.description = description;
        this.planDateStage0 = planDateStage0;
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
