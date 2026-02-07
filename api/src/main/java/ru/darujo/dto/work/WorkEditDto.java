package ru.darujo.dto.work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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

    //выдача прототипа
    private Timestamp issuePrototypeFact;
    //выдача прототипа план
    private Timestamp issuePrototypePlan;
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

    private Boolean rated;
    private Long projectId;
    private List<Long> projectList;


    public WorkEditDto(Long id,
                       Long codeSap,
                       String codeZI,
                       String name,
                       Timestamp analiseEndFact,
                       Timestamp analiseEndPlan,
                       Timestamp developEndFact,
                       Timestamp developEndPlan,
                       Timestamp issuePrototypeFact,
                       Timestamp issuePrototypePlan,
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
                       Long releaseId,
                       String release,
                       Timestamp issuingReleasePlan,
                       Timestamp issuingReleaseFact,
                       Timestamp analiseStartFact,
                       Timestamp developStartFact,
                       Timestamp debugStartFact,
                       Timestamp releaseStartFact,
                       Timestamp opeStartFact,
                       Timestamp analiseStartPlan,
                       Timestamp developStartPlan,
                       Timestamp debugStartPlan,
                       Timestamp releaseStartPlan,
                       Timestamp opeStartPlan,
                       Boolean rated,
                       Long projectId,
                       List<Long> projectList) {
        this.id = id;
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
    }

    public Long getId() {
        return id;
    }

    public Timestamp getIssuePrototypeFact() {
        return issuePrototypeFact;
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

    public String getName() {
        return name;
    }

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

    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    public Timestamp getIssuingReleaseFact() {
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

    public Timestamp getIssuePrototypePlan() {
        return issuePrototypePlan;
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

    public Timestamp getAnaliseStartFact() {
        return analiseStartFact;
    }

    public Timestamp getDevelopStartFact() {
        return developStartFact;
    }

    public Timestamp getDebugStartFact() {
        return debugStartFact;
    }

    public Timestamp getReleaseStartFact() {
        return releaseStartFact;
    }

    public Timestamp getOpeStartFact() {
        return opeStartFact;
    }

    public Timestamp getAnaliseStartPlan() {
        return analiseStartPlan;
    }

    public Timestamp getDevelopStartPlan() {
        return developStartPlan;
    }

    public Timestamp getDebugStartPlan() {
        return debugStartPlan;
    }

    public Timestamp getReleaseStartPlan() {
        return releaseStartPlan;
    }

    public Timestamp getOpeStartPlan() {
        return opeStartPlan;
    }

    public Timestamp getDevelopEndFact() {
        return developEndFact;
    }

    public Timestamp getDevelopEndPlan() {
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
}
