package ru.darujo.convertor;

import ru.darujo.dto.WorkDto;
import ru.darujo.dto.WorkEditDto;
import ru.darujo.dto.WorkLittleDto;
import ru.darujo.model.Work;


import java.util.Date;

public class WorkBuilder {
    private Long id;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Разработка прототипа
    private Date developEndFact;
    // Стабилизация прототипа
    private Date debugEndFact;
    // Стабилизация релиза
    private Date releaseEndFact;
    // ОПЭ релиза
    private Date opeEndFact;
    // ВЕНДЕРКА
    private Date analiseEndFact;
    // Разработка прототипа
    private Date developEndPlan;
    // Стабилизация прототипа
    private Date debugEndPlan;
    // Стабилизация релиза
    private Date releaseEndPlan;
    // ОПЭ релиза
    private Date opeEndPlan;
    // ВЕНДЕРКА
    private Date analiseEndPlan;
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

    public WorkBuilder setTask(String task) {
        this.task = task;
        return this;
    }

    public WorkBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkBuilder setPlanDateStage0(Date planDateStage0) {
        this.planDateStage0 = planDateStage0;
        return this;
    }

    public WorkBuilder setStartTaskPlan(Date startTaskPlan) {
        this.startTaskPlan = startTaskPlan;
        return this;
    }

    public WorkBuilder setStartTaskFact(Date startTaskFact) {
        this.startTaskFact = startTaskFact;
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

    public WorkBuilder setIssuingReleasePlan(Date issuingReleasePlan) {
        this.issuingReleasePlan = issuingReleasePlan;
        return this;
    }

    public WorkBuilder setIssuingReleaseFact(Date issuingReleaseFact) {
        this.issuingReleaseFact = issuingReleaseFact;
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

    public WorkBuilder setDevelopEndFact(Date developEndFact) {
        this.developEndFact = developEndFact;
        return this;
    }
    public WorkBuilder setDebugEndFact(Date debugEndFact) {
        this.debugEndFact = debugEndFact;
        return this;
    }
    public WorkBuilder setReleaseEndFact(Date releaseEndFact) {
        this.releaseEndFact = releaseEndFact;
        return this;
    }
    public WorkBuilder setOpeEndFact(Date opeEndFact) {
        this.opeEndFact = opeEndFact;
        return this;
    }
    public WorkBuilder setAnaliseEndFact(Date analiseEndFact) {
        this.analiseEndFact = analiseEndFact;
        return this;
    }

    public WorkBuilder setDevelopEndPlan(Date developEndPlan) {
        this.developEndPlan = developEndPlan;
        return this;
    }

    public WorkBuilder setDebugEndPlan(Date debugEndPlan) {
        this.debugEndPlan = debugEndPlan;
        return this;
    }

    public WorkBuilder setReleaseEndPlan(Date releaseEndPlan) {
        this.releaseEndPlan = releaseEndPlan;
        return this;
    }

    public WorkBuilder setOpeEndPlan(Date opeEndPlan) {
        this.opeEndPlan = opeEndPlan;
        return this;
    }

    public WorkBuilder setAnaliseEndPlan(Date analiseEndPlan) {
        this.analiseEndPlan = analiseEndPlan;
        return this;
    }

    public WorkBuilder setFactDateStage0(Date factDateStage0) {
        this.factDateStage0 = factDateStage0;
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
                analiseEndFact,
                developEndFact,
                debugEndFact,
                releaseEndFact,
                opeEndFact,
                task,
                description,
                planDateStage0,
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
                planDateStage0,
                factDateStage0,
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
                planDateStage0,
                factDateStage0,
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
}
