package ru.darujo.convertor;

import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.Release;
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
    private Timestamp issuePrototypeFact;
    // Стабилизация прототипа
    private Timestamp debugEndFact;
    // Стабилизация релиза
    private Timestamp releaseEndFact;
    // ОПЭ релиза
    private Timestamp opeEndFact;
    // ВЕНДЕРКА
    private Timestamp analiseEndFact;
    // Разработка прототипа
    private Timestamp issuePrototypePlan;
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
    // Текущий этап ЗИ
    private Integer stageZI;
    // Порядковый номер релиза
    private Long releaseId;
    private String release;
    // Выдача релиза даты План
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;
    // ВЕНДЕРКА Факт
    private Timestamp analiseStartFact;
    private Timestamp developStartFact;
    // Стабилизация прототипа Факт
    private Timestamp debugStartFact;
    // Стабилизация релиза Факт
    private Timestamp releaseStartFact;
    // ОПЭ релиза Факт
    private Timestamp opeStartFact;

    // ВЕНДЕРКА Факт
    private Timestamp analiseStartPlan;
    //начало разработки план
    private Timestamp developStartPlan;
    // Стабилизация релиза Факт
    private Timestamp debugStartPlan;
    // Стабилизация релиза plan
    private Timestamp releaseStartPlan;
    // ОПЭ релиза Факт
    private Timestamp opeStartPlan;

    private Timestamp developEndFact;
    //начало разработки план
    private Timestamp developEndPlan;
    private Boolean rated;

    public WorkBuilder setRated(Boolean rated) {
        this.rated = rated;
        return this;
    }

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

    public WorkBuilder setIssuePrototypeFact(Timestamp issuePrototypeFact) {
        this.issuePrototypeFact = dateToStartTime(issuePrototypeFact);
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

    public WorkBuilder setIssuePrototypePlan(Timestamp issuePrototypePlan) {
        this.issuePrototypePlan = dateToStartTime(issuePrototypePlan);
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

    public WorkBuilder setAnaliseStartFact(Timestamp analiseStartFact) {
        this.analiseStartFact = analiseStartFact;
        return this;
    }

    public WorkBuilder setDevelopStartFact(Timestamp developStartFact) {
        this.developStartFact = developStartFact;
        return this;
    }

    public WorkBuilder setDebugStartFact(Timestamp debugStartFact) {
        this.debugStartFact = debugStartFact;
        return this;
    }

    public WorkBuilder setReleaseStartFact(Timestamp releaseStartFact) {
        this.releaseStartFact = releaseStartFact;
        return this;
    }

    public WorkBuilder setOpeStartFact(Timestamp opeStartFact) {
        this.opeStartFact = opeStartFact;
        return this;
    }

    public WorkBuilder setAnaliseStartPlan(Timestamp analiseStartPlan) {
        this.analiseStartPlan = analiseStartPlan;
        return this;
    }

    public WorkBuilder setDevelopStartPlan(Timestamp developStartPlan) {
        this.developStartPlan = developStartPlan;
        return this;
    }

    public WorkBuilder setDebugStartPlan(Timestamp debugStartPlan) {
        this.debugStartPlan = debugStartPlan;
        return this;
    }

    public WorkBuilder setReleaseStartPlan(Timestamp releaseStartPlan) {
        this.releaseStartPlan = releaseStartPlan;
        return this;
    }

    public WorkBuilder setOpeStartPlan(Timestamp opeStartPlan) {
        this.opeStartPlan = opeStartPlan;
        return this;
    }

    public static WorkBuilder createWork() {
        return new WorkBuilder();
    }

    public WorkDto getWorkDto() {
        return new WorkDto(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndPlan,
                analiseEndFact,
                issuePrototypeFact,
                debugEndFact,
                releaseEndFact,
                opeEndFact,
                task,
                description,
                startTaskPlan,
                startTaskFact,
                stageZI,
                release,
                issuingReleasePlan,
                issuingReleaseFact,
                rated);
    }

    public WorkEditDto getWorkEditDto() {
        return new WorkEditDto(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndFact,
                analiseEndPlan,
                developEndFact,
                developEndPlan,
                issuePrototypeFact,
                issuePrototypePlan,
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
                stageZI,
                releaseId,
                release,
                issuingReleasePlan,
                issuingReleaseFact,
                analiseStartFact,
                developStartFact,
                debugStartFact,
                releaseStartFact,
                opeStartFact,
                analiseStartPlan,
                developStartPlan,
                debugStartPlan,
                releaseStartPlan,
                opeStartPlan,
                rated);
    }

    public Work getWork() {
        analiseEndFact = getDate(analiseEndFact, developStartFact);
//        issuePrototypeFact = getDate(issuePrototypeFact, debugStartFact);
        debugStartFact = getDateAddDay(debugStartFact, issuePrototypeFact);
        debugEndFact = getDate(debugEndFact, releaseStartFact);
        releaseEndFact = getDate(releaseEndFact, opeStartFact);

        analiseEndPlan = getDate(analiseEndPlan, developStartPlan);
//        issuePrototypePlan = getDate(issuePrototypePlan, debugStartPlan);
        debugStartPlan = getDateAddDay(debugStartPlan, issuePrototypePlan);
        debugEndPlan = getDate(debugEndPlan, releaseStartPlan);
        releaseEndPlan = getDate(releaseEndPlan, opeStartPlan);

        return new Work(
                id,
                codeSap,
                codeZI,
                name,
                analiseEndFact,
                analiseEndPlan,
                developEndFact,
                developEndPlan,
                issuePrototypeFact,
                issuePrototypePlan,
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
                stageZI,
                releaseId != null ? new Release(releaseId, release, issuingReleasePlan, issuingReleaseFact) : null,
                analiseStartFact,
                developStartFact,
                debugStartFact,
                releaseStartFact,
                opeStartFact,
                analiseStartPlan,
                developStartPlan,
                debugStartPlan,
                releaseStartPlan,
                opeStartPlan,
                rated);
    }

    public WorkLittleDto getWorkLittleDto() {
        return new WorkLittleDto(id, codeSap, codeZI, name, stageZI, rated);
    }

    public Timestamp dateToStartTime(Timestamp timestamp) {
        if (timestamp == null) {
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

    public WorkBuilder setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
        return this;
    }

    public Timestamp getDate(Timestamp date, Timestamp dateMinus) {
        if (date != null) {
            return date;
        }
        if (dateMinus == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateMinus);
        cal.add(Calendar.DATE, -1);
        return new Timestamp(cal.getTimeInMillis());

    }

    public Timestamp getDateAddDay(Timestamp date, Timestamp datePlus) {

        if (datePlus == null) {
            return date;
        } else {
            if (date != null && date.after(datePlus)) {
                return date;
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(datePlus);
        cal.add(Calendar.DATE, 1);
        return new Timestamp(cal.getTimeInMillis());

    }

}
