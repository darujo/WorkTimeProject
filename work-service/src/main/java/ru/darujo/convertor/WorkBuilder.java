package ru.darujo.convertor;

import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.Work;


import java.sql.Timestamp;
import java.util.Calendar;

public class WorkBuilder {
    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Разработка прототипа
    private Timestamp developEndFact;
    // Стабилизация прототипа
    private Timestamp debugEndFact;
    // Стабилизация релиза
    private Timestamp releaseEndFact;
    // ОПЭ релиза
    private Timestamp opeEndFact;
    // ВЕНДЕРКА
    private Timestamp analiseEndFact;
    // Разработка прототипа
    private Timestamp developEndPlan;
    // Стабилизация прототипа
    private Timestamp debugEndPlan;
    // Стабилизация релиза
    private Timestamp releaseEndPlan;
    // ОПЭ релиза
    private Timestamp opeEndPlan;
    // ВЕНДЕРКА
    private Timestamp analiseEndPlan;
    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;
    // Дата начала доработки План
    private Timestamp startTaskPlan;
    // Дата начала доработки Факт
    private Timestamp startTaskFact;
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

    public WorkBuilder setTask(String task) {
        this.task = task;
        return this;
    }

    public WorkBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkBuilder setStartTaskPlan(Timestamp startTaskPlan) {
        this.startTaskPlan = dateToStartTime(startTaskPlan);
        return this;
    }

    public WorkBuilder setStartTaskFact(Timestamp startTaskFact) {
        this.startTaskFact = dateToStartTime(startTaskFact);
        return this;
    }

    public WorkBuilder setLaborDevelop(Float laborDevelop) {
        this.laborDevelop = laborDevelop;
        return this;
    }

    public WorkBuilder setLaborDebug(Float laborDebug) {
        this.laborDebug = laborDebug;
        return this;
    }

    public WorkBuilder setLaborRelease(Float laborRelease) {
        this.laborRelease = laborRelease;
        return this;
    }

    public WorkBuilder setLaborOPE(Float laborOPE) {
        this.laborOPE = laborOPE;
        return this;
    }

    public WorkBuilder setStageZI(Integer stageZI) {
        this.stageZI = stageZI;
        return this;
    }

    public WorkBuilder setRelease(String release) {
        this.release = release;
        return this;
    }

    public WorkBuilder setIssuingReleasePlan(Timestamp issuingReleasePlan) {
        this.issuingReleasePlan = dateToStartTime(issuingReleasePlan);
        return this;
    }

    public WorkBuilder setIssuingReleaseFact(Timestamp issuingReleaseFact) {
        this.issuingReleaseFact = dateToStartTime(issuingReleaseFact);
        return this;
    }

    public WorkBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkBuilder setCodeSap(Long codeSap) {
        this.codeSap = codeSap;
        return this;
    }

    public WorkBuilder setCodeZI(String codeZI) {
        this.codeZI = codeZI;
        return this;
    }

    public WorkBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public WorkBuilder setDevelopEndFact(Timestamp developEndFact) {
        this.developEndFact = dateToStartTime(developEndFact);
        return this;
    }
    public WorkBuilder setDebugEndFact(Timestamp debugEndFact) {
        this.debugEndFact = dateToStartTime(debugEndFact);
        return this;
    }
    public WorkBuilder setReleaseEndFact(Timestamp releaseEndFact) {
        this.releaseEndFact = dateToStartTime(releaseEndFact);
        return this;
    }
    public WorkBuilder setOpeEndFact(Timestamp opeEndFact) {
        this.opeEndFact = dateToStartTime(opeEndFact);
        return this;
    }
    public WorkBuilder setAnaliseEndFact(Timestamp analiseEndFact) {
        this.analiseEndFact = dateToStartTime(analiseEndFact);
        return this;
    }

    public WorkBuilder setDevelopEndPlan(Timestamp developEndPlan) {
        this.developEndPlan = dateToStartTime(developEndPlan);
        return this;
    }

    public WorkBuilder setDebugEndPlan(Timestamp debugEndPlan) {
        this.debugEndPlan = dateToStartTime(debugEndPlan);
        return this;
    }

    public WorkBuilder setReleaseEndPlan(Timestamp releaseEndPlan) {
        this.releaseEndPlan = dateToStartTime(releaseEndPlan);
        return this;
    }

    public WorkBuilder setOpeEndPlan(Timestamp opeEndPlan) {
        this.opeEndPlan = dateToStartTime(opeEndPlan);
        return this;
    }

    public WorkBuilder setAnaliseEndPlan(Timestamp analiseEndPlan) {
        this.analiseEndPlan = dateToStartTime(analiseEndPlan);
        return this;
    }

    public static WorkBuilder createWork () {
        return new WorkBuilder();
    }
    public WorkDto getWorkDto(){
        return new WorkDto(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndPlan,
                analiseEndFact,
                developEndFact,
                debugEndFact,
                releaseEndFact,
                opeEndFact,
                task,
                description,
                startTaskPlan,
                startTaskFact,
                laborDevelop,
                laborDebug,
                laborRelease,
                laborOPE,
                stageZI,
                release,
                issuingReleasePlan,
                issuingReleaseFact);
    }
    public WorkEditDto getWorkEditDto(){
        return new WorkEditDto(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndFact,
                analiseEndPlan,
                developEndFact,
                developEndPlan,
                debugEndFact,
                debugEndPlan,
                releaseEndFact,
                releaseEndPlan,
                opeEndFact,
                opeEndPlan,
                task,
                description,
                startTaskPlan,
                startTaskFact,
                laborDevelop,
                laborDebug,
                laborRelease,
                laborOPE,
                stageZI,
                release,
                issuingReleasePlan,
                issuingReleaseFact);
    }
    public Work getWork(){
        return new Work(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndFact,
                analiseEndPlan,
                developEndFact,
                developEndPlan,
                debugEndFact,
                debugEndPlan,
                releaseEndFact,
                releaseEndPlan,
                opeEndFact,
                opeEndPlan,
                task,
                description,
                startTaskPlan,
                startTaskFact,
                laborDevelop,
                laborDebug,
                laborRelease,
                laborOPE,
                stageZI,
                release,
                issuingReleasePlan,
                issuingReleaseFact);
    }

    public WorkLittleDto getWorkLittleDto() {
        return new WorkLittleDto(id, codeSap, codeZI, name, stageZI);
    }
    public Timestamp dateToStartTime(Timestamp timestamp) {
        if(timestamp== null){
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }
}
