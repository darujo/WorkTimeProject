package ru.darujo.dto.work;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class WorkEditDto implements Serializable, WorkPlanTime {
    private Long id;
    private Long workProjectId;
    // Код SAP
    private Long codeSap;
    // Код Зи
    private String codeZI;
    // Разработка прототипа
    private String name;
    // Анализ факт
    private ZonedDateTime analiseEndFact;
    // Анализ План
    private ZonedDateTime analiseEndPlan;
    //конец разработки факт
    private ZonedDateTime developEndFact;
    //конец разработки план
    private ZonedDateTime developEndPlan;

    //выдача прототипа
    private ZonedDateTime issuePrototypeFact;
    //выдача прототипа план
    private ZonedDateTime issuePrototypePlan;
    // конец Стабилизация прототипа факт
    private ZonedDateTime debugEndFact;
    // конец Стабилизация прототипа план
    private ZonedDateTime debugEndPlan;
    // конец Стабилизация релиза
    private ZonedDateTime releaseEndFact;
    // конец Стабилизация релиза
    private ZonedDateTime releaseEndPlan;
    // конец ОПЭ релиза
    private ZonedDateTime opeEndFact;
    // конец ОПЭ релиза
    private ZonedDateTime opeEndPlan;
    // № внутренней задачи (DEVBO)
    private String task;
    // Краткое описание внутренней задачи
    private String description;

    // Дата начала доработки План
    private ZonedDateTime startTaskPlan;
    // Дата начала доработки Факт
    private ZonedDateTime startTaskFact;
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
    private Long releaseId;
    private String release;
    // Выдача релиза даты План
    private ZonedDateTime issuingReleasePlan;
    // Выдача релиза дата факт
    private ZonedDateTime issuingReleaseFact;

    // Анализ Факт
    private ZonedDateTime analiseStartFact;
    private ZonedDateTime developStartFact;
    // Стабилизация прототипа Факт
    private ZonedDateTime debugStartFact;
    // Стабилизация релиза Факт
    private ZonedDateTime releaseStartFact;
    // ОПЭ релиза Факт
    private ZonedDateTime opeStartFact;

    // Анализ план
    private ZonedDateTime analiseStartPlan;
    //начало разработки план
    private ZonedDateTime developStartPlan;
    // Стабилизация релиза план
    private ZonedDateTime debugStartPlan;
    // Стабилизация релиза план
    private ZonedDateTime releaseStartPlan;
    // ОПЭ релиза план
    private ZonedDateTime opeStartPlan;

    private Boolean rated;
    private Long projectId;
    private List<Long> projectList;

    private WorkLittleDto parentWork;
    private List<WorkLittleDto> childWork;


    public WorkEditDto(Long id,
                       Long workProjectId,
                       Long codeSap,
                       String codeZI,
                       String name,
                       ZonedDateTime analiseEndFact,
                       ZonedDateTime analiseEndPlan,
                       ZonedDateTime developEndFact,
                       ZonedDateTime developEndPlan,
                       ZonedDateTime issuePrototypeFact,
                       ZonedDateTime issuePrototypePlan,
                       ZonedDateTime debugEndFact,
                       ZonedDateTime debugEndPlan,
                       ZonedDateTime releaseEndFact,
                       ZonedDateTime releaseEndPlan,
                       ZonedDateTime opeEndFact,
                       ZonedDateTime opeEndPlan,
                       String task,
                       String description,
                       ZonedDateTime startTaskPlan,
                       ZonedDateTime startTaskFact,
                       Integer stageZI,
                       Long releaseId,
                       String release,
                       ZonedDateTime issuingReleasePlan,
                       ZonedDateTime issuingReleaseFact,
                       ZonedDateTime analiseStartFact,
                       ZonedDateTime developStartFact,
                       ZonedDateTime debugStartFact,
                       ZonedDateTime releaseStartFact,
                       ZonedDateTime opeStartFact,
                       ZonedDateTime analiseStartPlan,
                       ZonedDateTime developStartPlan,
                       ZonedDateTime debugStartPlan,
                       ZonedDateTime releaseStartPlan,
                       ZonedDateTime opeStartPlan,
                       Boolean rated,
                       Long projectId,
                       List<Long> projectList,
                       WorkLittleDto parentWork,
                       List<WorkLittleDto> childWork
    ) {
        this.id = id;
        this.workProjectId = workProjectId;
        this.codeSap = codeSap;
        this.codeZI = codeZI;
        this.name = name;
        this.analiseEndFact = analiseEndFact;
        this.analiseEndPlan = analiseEndPlan;
        this.developEndFact = developEndFact;
        this.developEndPlan = developEndPlan;
        this.issuePrototypeFact = issuePrototypeFact;
        this.issuePrototypePlan = issuePrototypePlan;
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
        this.releaseId = releaseId;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.analiseStartFact = analiseStartFact;
        this.developStartFact = developStartFact;
        this.debugStartFact = debugStartFact;
        this.releaseStartFact = releaseStartFact;
        this.opeStartFact = opeStartFact;
        this.analiseStartPlan = analiseStartPlan;
        this.developStartPlan = developStartPlan;
        this.debugStartPlan = debugStartPlan;
        this.releaseStartPlan = releaseStartPlan;
        this.opeStartPlan = opeStartPlan;
        this.rated = rated;
        this.projectId = projectId;
        this.projectList = projectList;
        this.parentWork = parentWork;
        this.childWork = childWork;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getIssuePrototypeFact() {
        return issuePrototypeFact;
    }

    public ZonedDateTime getDebugEndFact() {
        return debugEndFact;
    }

    public ZonedDateTime getReleaseEndFact() {
        return releaseEndFact;
    }

    public ZonedDateTime getOpeEndFact() {
        return opeEndFact;
    }

    public ZonedDateTime getAnaliseEndFact() {
        return analiseEndFact;
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

    public ZonedDateTime getStartTaskPlan() {
        return startTaskPlan;
    }

    public ZonedDateTime getStartTaskFact() {
        return startTaskFact;
    }

    @SuppressWarnings("unused")
    public Float getLaborAnalise() {
        return laborAnalise;
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

    public Integer getStageZI() {
        return stageZI;
    }

    public String getRelease() {
        return release;
    }

    public ZonedDateTime getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    public ZonedDateTime getIssuingReleaseFact() {
        return issuingReleaseFact;
    }


    @SuppressWarnings("unused")
    public WorkEditDto() {
    }

    @SuppressWarnings("unused")
    public Boolean getRated() {
        return rated;
    }

    public Long getCodeSap() {
        return codeSap;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public ZonedDateTime getIssuePrototypePlan() {
        return issuePrototypePlan;
    }

    public ZonedDateTime getDebugEndPlan() {
        return debugEndPlan;
    }

    public ZonedDateTime getReleaseEndPlan() {
        return releaseEndPlan;
    }

    public ZonedDateTime getOpeEndPlan() {
        return opeEndPlan;
    }

    public ZonedDateTime getAnaliseEndPlan() {
        return analiseEndPlan;
    }

    public Long getReleaseId() {
        return releaseId;
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

    public ZonedDateTime getAnaliseStartFact() {
        return analiseStartFact;
    }

    public ZonedDateTime getDevelopStartFact() {
        return developStartFact;
    }

    public ZonedDateTime getDebugStartFact() {
        return debugStartFact;
    }

    public ZonedDateTime getReleaseStartFact() {
        return releaseStartFact;
    }

    public ZonedDateTime getOpeStartFact() {
        return opeStartFact;
    }

    public ZonedDateTime getAnaliseStartPlan() {
        return analiseStartPlan;
    }

    public ZonedDateTime getDevelopStartPlan() {
        return developStartPlan;
    }

    public ZonedDateTime getDebugStartPlan() {
        return debugStartPlan;
    }

    public ZonedDateTime getReleaseStartPlan() {
        return releaseStartPlan;
    }

    public ZonedDateTime getOpeStartPlan() {
        return opeStartPlan;
    }

    public ZonedDateTime getDevelopEndFact() {
        return developEndFact;
    }

    public ZonedDateTime getDevelopEndPlan() {
        return developEndPlan;
    }

    public List<Long> getProjectList() {
        return projectList;
    }

    @Override
    public Long getProjectId() {
        return projectId;
    }

    @Override
    public Long getWorkId() {
        return id;
    }

    @SuppressWarnings("unused")
    public Long getWorkProjectId() {
        return workProjectId;
    }

    public WorkLittleDto getParentWork() {
        return parentWork;
    }

    public List<WorkLittleDto> getChildWork() {
        return childWork;
    }

    @Override
    public List<Long> getChildId() {
        return childWork == null || childWork.isEmpty() ? null : childWork.stream().map(WorkLittleDto::getId).toList();
    }
}
