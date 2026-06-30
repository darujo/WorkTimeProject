package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.work.WorkDto;
import ru.darujo.dto.work.WorkEditDto;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.model.Release;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;
import ru.darujo.model.WorkProject;

import java.time.LocalDate;
import java.util.List;

public class WorkBuilder {
    private Long id;
    private Long workProjectId;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Разработка прототипа
    private LocalDate issuePrototypeFact;
    // Стабилизация прототипа
    private LocalDate debugEndFact;
    // Стабилизация релиза
    private LocalDate releaseEndFact;
    // ОПЭ релиза
    private LocalDate opeEndFact;
    // Анализ
    private LocalDate analiseEndFact;
    // Разработка прототипа
    private LocalDate issuePrototypePlan;
    // Стабилизация прототипа
    private LocalDate debugEndPlan;
    // Стабилизация релиза
    private LocalDate releaseEndPlan;
    // ОПЭ релиза
    private LocalDate opeEndPlan;
    // Анализ
    private LocalDate analiseEndPlan;
    // № внутренней задачи (D E V B O)
    private String task;
    // Краткое описание внутренней задачи
    private String description;
    // Дата начала доработки План
    private LocalDate startTaskPlan;
    // Дата начала доработки Факт
    private LocalDate startTaskFact;
    // Текущий этап ЗИ
    private Integer stageZI;
    // Порядковый номер релиза
    private Long releaseId;
    private String release;
    // Выдача релиза даты План
    private LocalDate issuingReleasePlan;
    // Выдача релиза дата факт
    private LocalDate issuingReleaseFact;
    // Анализ Факт
    private LocalDate analiseStartFact;
    private LocalDate developStartFact;
    // Стабилизация прототипа Факт
    private LocalDate debugStartFact;
    // Стабилизация релиза Факт
    private LocalDate releaseStartFact;
    // ОПЭ релиза Факт
    private LocalDate opeStartFact;

    // Анализ план
    private LocalDate analiseStartPlan;
    //начало разработки план
    private LocalDate developStartPlan;
    // Стабилизация релиза план
    private LocalDate debugStartPlan;
    // Стабилизация релиза план
    private LocalDate releaseStartPlan;
    // ОПЭ релиза план
    private LocalDate opeStartPlan;

    private LocalDate developEndFact;
    //начало разработки план
    private LocalDate developEndPlan;
    private Boolean rated;
    private List<Long> projectList;
    private Long projectId;
    private WorkLittleDto workParentDto;
    private List<WorkLittleDto> childWorkDto;

    private WorkLittle workParent;
    private List<WorkLittle> childWork;

    public WorkBuilder setWorkParentDto(WorkLittleDto workParentDto) {
        this.workParentDto = workParentDto;
        return this;
    }

    public WorkBuilder setChildWorkDto(List<WorkLittleDto> childWorkDto) {
        this.childWorkDto = childWorkDto;
        return this;
    }

    public WorkBuilder setWorkParent(WorkLittle workParent) {
        this.workParent = workParent;
        return this;
    }

    public WorkBuilder setChildWork(List<WorkLittle> childWork) {
        this.childWork = childWork;
        return this;
    }

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

    public WorkBuilder setStartTaskPlan(LocalDate startTaskPlan) {
        this.startTaskPlan = dateToStartTime(startTaskPlan);
        return this;
    }

    public WorkBuilder setStartTaskFact(LocalDate startTaskFact) {
        this.startTaskFact = dateToStartTime(startTaskFact);
        return this;
    }


    public WorkBuilder setStageZI(Integer stageZI) {
        this.stageZI = stageZI;
        return this;
    }

    public WorkBuilder setWorkProjectId(Long workProjectId) {
        this.workProjectId = workProjectId;
        return this;
    }

    public WorkBuilder setProjectList(List<Long> projectList) {
        this.projectList = projectList;
        return this;
    }

    public WorkBuilder setProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public WorkBuilder setRelease(String release) {
        this.release = release;
        return this;
    }

    public WorkBuilder setIssuingReleasePlan(LocalDate issuingReleasePlan) {
        this.issuingReleasePlan = dateToStartTime(issuingReleasePlan);
        return this;
    }

    public WorkBuilder setIssuingReleaseFact(LocalDate issuingReleaseFact) {
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

    public WorkBuilder setDevelopEndFact(LocalDate developEndFact) {
        this.developEndFact = dateToStartTime(developEndFact);
        return this;
    }

    public WorkBuilder setIssuePrototypeFact(LocalDate issuePrototypeFact) {
        this.issuePrototypeFact = dateToStartTime(issuePrototypeFact);
        return this;
    }

    public WorkBuilder setDebugEndFact(LocalDate debugEndFact) {
        this.debugEndFact = dateToStartTime(debugEndFact);
        return this;
    }

    public WorkBuilder setReleaseEndFact(LocalDate releaseEndFact) {
        this.releaseEndFact = dateToStartTime(releaseEndFact);
        return this;
    }

    public WorkBuilder setOpeEndFact(LocalDate opeEndFact) {
        this.opeEndFact = dateToStartTime(opeEndFact);
        return this;
    }

    public WorkBuilder setAnaliseEndFact(LocalDate analiseEndFact) {
        this.analiseEndFact = dateToStartTime(analiseEndFact);
        return this;
    }

    public WorkBuilder setDevelopEndPlan(LocalDate developEndPlan) {
        this.developEndPlan = dateToStartTime(developEndPlan);
        return this;
    }

    public WorkBuilder setIssuePrototypePlan(LocalDate issuePrototypePlan) {
        this.issuePrototypePlan = dateToStartTime(issuePrototypePlan);
        return this;
    }

    public WorkBuilder setDebugEndPlan(LocalDate debugEndPlan) {
        this.debugEndPlan = dateToStartTime(debugEndPlan);
        return this;
    }

    public WorkBuilder setReleaseEndPlan(LocalDate releaseEndPlan) {
        this.releaseEndPlan = dateToStartTime(releaseEndPlan);
        return this;
    }

    public WorkBuilder setOpeEndPlan(LocalDate opeEndPlan) {
        this.opeEndPlan = dateToStartTime(opeEndPlan);
        return this;
    }

    public WorkBuilder setAnaliseEndPlan(LocalDate analiseEndPlan) {
        this.analiseEndPlan = dateToStartTime(analiseEndPlan);
        return this;
    }

    public WorkBuilder setAnaliseStartFact(LocalDate analiseStartFact) {
        this.analiseStartFact = analiseStartFact;
        return this;
    }

    public WorkBuilder setDevelopStartFact(LocalDate developStartFact) {
        this.developStartFact = developStartFact;
        return this;
    }

    public WorkBuilder setDebugStartFact(LocalDate debugStartFact) {
        this.debugStartFact = debugStartFact;
        return this;
    }

    public WorkBuilder setReleaseStartFact(LocalDate releaseStartFact) {
        this.releaseStartFact = releaseStartFact;
        return this;
    }

    public WorkBuilder setOpeStartFact(LocalDate opeStartFact) {
        this.opeStartFact = opeStartFact;
        return this;
    }

    public WorkBuilder setAnaliseStartPlan(LocalDate analiseStartPlan) {
        this.analiseStartPlan = analiseStartPlan;
        return this;
    }

    public WorkBuilder setDevelopStartPlan(LocalDate developStartPlan) {
        this.developStartPlan = developStartPlan;
        return this;
    }

    public WorkBuilder setDebugStartPlan(LocalDate debugStartPlan) {
        this.debugStartPlan = debugStartPlan;
        return this;
    }

    public WorkBuilder setReleaseStartPlan(LocalDate releaseStartPlan) {
        this.releaseStartPlan = releaseStartPlan;
        return this;
    }

    public WorkBuilder setOpeStartPlan(LocalDate opeStartPlan) {
        this.opeStartPlan = opeStartPlan;
        return this;
    }

    public static WorkBuilder createWork() {
        return new WorkBuilder();
    }

    public WorkDto getWorkDto() {
        return new WorkDto(
                id,
                projectId,
                codeSap,
                codeZI,
                name,
                DateHelper.getZDT(analiseEndPlan),
                DateHelper.getZDT(analiseEndFact),
                DateHelper.getZDT(issuePrototypeFact),
                DateHelper.getZDT(debugEndFact),
                DateHelper.getZDT(releaseEndFact),
                DateHelper.getZDT(opeEndFact),
                task,
                description,
                DateHelper.getZDT(startTaskPlan),
                DateHelper.getZDT(startTaskFact),
                stageZI,
                release,
                DateHelper.getZDT(issuingReleasePlan),
                DateHelper.getZDT(issuingReleaseFact),
                rated,
                childWork == null || childWork.isEmpty() ? null : childWork.stream().map(WorkLittle::getId).toList());
    }

    public WorkEditDto getWorkEditDto() {
        return new WorkEditDto(
                id,
                workProjectId,
                codeSap,
                codeZI,
                name,
                DateHelper.getZDT(analiseEndFact),
                DateHelper.getZDT(analiseEndPlan),
                DateHelper.getZDT(developEndFact),
                DateHelper.getZDT(developEndPlan),
                DateHelper.getZDT(issuePrototypeFact),
                DateHelper.getZDT(issuePrototypePlan),
                DateHelper.getZDT(debugEndFact),
                DateHelper.getZDT(debugEndPlan),
                DateHelper.getZDT(releaseEndFact),
                DateHelper.getZDT(releaseEndPlan),
                DateHelper.getZDT(opeEndFact),
                DateHelper.getZDT(opeEndPlan),
                task,
                description,
                DateHelper.getZDT(startTaskPlan),
                DateHelper.getZDT(startTaskFact),
                stageZI,
                releaseId,
                release,
                DateHelper.getZDT(issuingReleasePlan),
                DateHelper.getZDT(issuingReleaseFact),
                DateHelper.getZDT(analiseStartFact),
                DateHelper.getZDT(developStartFact),
                DateHelper.getZDT(debugStartFact),
                DateHelper.getZDT(releaseStartFact),
                DateHelper.getZDT(opeStartFact),
                DateHelper.getZDT(analiseStartPlan),
                DateHelper.getZDT(developStartPlan),
                DateHelper.getZDT(debugStartPlan),
                DateHelper.getZDT(releaseStartPlan),
                DateHelper.getZDT(opeStartPlan),
                rated,
                projectId,
                projectList,
                workParentDto,
                childWorkDto);
    }

    private Work getWork() {
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
                description,
                projectList,
                releaseId == null ? null : new Release(releaseId, release),
                workParent,
                childWork);
    }

    public WorkLittleDto getWorkLittleDto() {
        return new WorkLittleDto(id, codeSap, codeZI, name, stageZI, rated, projectList, workParentDto, childWorkDto);
    }

    public WorkLittle getWorkParent() {
        return new WorkLittle(id, codeSap, codeZI, name, projectList, releaseId == null ? null : new Release(releaseId, release), null, null);
    }

    public LocalDate dateToStartTime(LocalDate localDate) {
        return localDate;
    }

    public WorkBuilder setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
        return this;
    }

    public LocalDate getDate(LocalDate date, LocalDate dateMinus) {
        if (date != null) {
            return date;
        }
        if (dateMinus == null) {
            return null;
        }
        return dateMinus.minusDays(1);

    }

    public LocalDate getDateAddDay(LocalDate date, LocalDate datePlus) {

        if (datePlus == null) {
            return date;
        } else {
            if (date != null && date.isAfter(datePlus)) {
                return date;
            }
        }
        return datePlus.plusDays(1);

    }

    public WorkProject getWorkProject() {
        return new WorkProject(workProjectId,
                projectId,
                getWork(),
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
                rated,
                startTaskPlan,
                startTaskFact,
                task,
                stageZI);
    }
}
