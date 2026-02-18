package ru.darujo.dto.workrep;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.work.WorkPlanTime;

import java.sql.Timestamp;

public class WorkRepProjectDto implements WorkPlanTime, ProjectUpdateInter {
    @SuppressWarnings("unused")
    public WorkRepProjectDto() {
    }

    private Long workId;
    private Long projectId;
    private String projectCode;
    private String projectName;
    // Дата начала доработки План
    private Timestamp startTaskPlan;
    @SuppressWarnings("unused")
    private String startTaskPlanStr;
    // Дата начала доработки Факт
    private Timestamp startTaskFact;
    @SuppressWarnings("unused")
    private String startTaskFactStr;
    // ВЕНДЕРКА План
    private Timestamp analiseEndPlan;
    @SuppressWarnings("unused")
    private String analiseEndPlanStr;
    // ВЕНДЕРКА
    private Timestamp analiseEndFact;
    @SuppressWarnings("unused")
    private String analiseEndFactStr;
    // Плановые трудозатраты, чел/час анализ
    private Float laborAnalise;
    // Плановые трудозатраты, чел/час разработки
    private Float laborDevelop;
    //начало разработки план
    private Timestamp developEndPlan;
    @SuppressWarnings("unused")
    private String developEndPlanStr;
    //начало разработки факт
    private Timestamp developEndFact;
    @SuppressWarnings("unused")
    private String developEndFactStr;
    // Стабилизация прототипа план
    private Timestamp debugEndPlan;
    @SuppressWarnings("unused")
    private String debugEndPlanStr;
    // Стабилизация прототипа факт
    private Timestamp debugEndFact;
    @SuppressWarnings("unused")
    private String debugEndFactStr;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Timestamp issuingReleasePlan;
    @SuppressWarnings("unused")
    private String issuingReleasePlanStr;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;
    @SuppressWarnings("unused")
    private String issuingReleaseFactStr;
    // Стабилизация релиза
    private Timestamp releaseEndPlan;
    @SuppressWarnings("unused")
    private String releaseEndPlanStr;
    // Стабилизация релиза
    private Timestamp releaseEndFact;
    @SuppressWarnings("unused")
    private String releaseEndFactStr;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // ОПЭ релиза
    private Timestamp opeEndPlan;
    @SuppressWarnings("unused")
    private String opeEndPlanStr;
    // ОПЭ релиза
    private Timestamp opeEndFact;
    @SuppressWarnings("unused")
    private String opeEndFactStr;
    // Плановые трудозатраты, чел/час ОПЭ
    private Float laborOPE;
    // фактическое время
    private Float timeFact;


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
    private Timestamp issuePrototypePlan;
    @SuppressWarnings("unused")
    private String issuePrototypePlanStr;
    private Timestamp issuePrototypeFact;
    @SuppressWarnings("unused")
    private String issuePrototypeFactStr;

    public WorkRepProjectDto(Long workId,
                             Long projectId,
                             Timestamp startTaskPlan,
                             Timestamp startTaskFact,
                             Timestamp analiseEndPlan,
                             Timestamp analiseEndFact,
                             Timestamp developEndPlan,
                             Timestamp developEndFact,
                             Timestamp debugEndPlan,
                             Timestamp debugEndFact,
                             String release,
                             Timestamp issuingReleasePlan,
                             Timestamp issuingReleaseFact,
                             Timestamp releaseEndPlan,
                             Timestamp releaseEndFact,
                             Timestamp opeEndPlan,
                             Timestamp opeEndFact,
                             Float timeAnalise,
                             Float timeDevelop,
                             Float timeDebug,
                             Float timeRelease,
                             Float timeOPE,
                             Float timeWender,
                             Timestamp issuePrototypePlan,
                             Timestamp issuePrototypeFact) {
        this.workId = workId;
        this.projectId = projectId;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.analiseEndPlan = analiseEndPlan;
        this.analiseEndFact = analiseEndFact;
        this.developEndPlan = developEndPlan;
        this.developEndFact = developEndFact;
        this.debugEndPlan = debugEndPlan;
        this.debugEndFact = debugEndFact;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.releaseEndPlan = releaseEndPlan;
        this.releaseEndFact = releaseEndFact;
        this.opeEndPlan = opeEndPlan;
        this.opeEndFact = opeEndFact;
        this.timeAnalise = timeAnalise;
        this.timeDevelop = timeDevelop;
        this.timeDebug = timeDebug;
        this.timeRelease = timeRelease;
        this.timeOPE = timeOPE;
        this.timeWender = timeWender;
        this.timeFact = 0f;
        this.issuePrototypePlan = issuePrototypePlan;
        this.issuePrototypeFact = issuePrototypeFact;
        addTimeFact(timeAnalise);
        addTimeFact(timeDevelop);
        addTimeFact(timeDebug);
        addTimeFact(timeRelease);
        addTimeFact(timeOPE);
        addTimeFact(timeWender);
    }

    public void addTimeFact(Float time) {
        if (time != null) {
            timeFact = timeFact + time;
        }
    }

    public Float addTimePlan(Float time) {
        if (time != null) {
            return time;
        }
        return 0f;
    }

    @SuppressWarnings("unused")
    public String getStartTaskPlanStr() {
        return DateHelper.dateToDDMMYYYY(startTaskPlan);
    }

    @SuppressWarnings("unused")
    public String getStartTaskFactStr() {
        return DateHelper.dateToDDMMYYYY(startTaskFact);
    }

    @SuppressWarnings("unused")
    public String getDevelopEndPlanStr() {
        return DateHelper.dateToDDMMYYYY(developEndPlan);
    }

    @SuppressWarnings("unused")
    public String getDevelopEndFactStr() {
        return DateHelper.dateToDDMMYYYY(developEndFact);
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
    public String getDebugEndPlanStr() {
        return DateHelper.dateToDDMMYYYY(debugEndPlan);
    }

    @SuppressWarnings("unused")
    public String getDebugEndFactStr() {
        return DateHelper.dateToDDMMYYYY(debugEndFact);
    }

    @SuppressWarnings("unused")
    public String getReleaseEndPlanStr() {
        return DateHelper.dateToDDMMYYYY(releaseEndPlan);
    }

    @SuppressWarnings("unused")
    public String getReleaseEndFactStr() {
        return DateHelper.dateToDDMMYYYY(releaseEndFact);
    }

    @SuppressWarnings("unused")
    public Float getLaborDebug() {
        return laborDebug;
    }

    @SuppressWarnings("unused")
    public String getRelease() {
        return release;
    }

    @SuppressWarnings("unused")
    public String getIssuingReleasePlanStr() {
        return DateHelper.dateToDDMMYYYY(issuingReleasePlan);
    }

    @SuppressWarnings("unused")
    public String getIssuingReleaseFactStr() {
        return DateHelper.dateToDDMMYYYY(issuingReleaseFact);
    }

    @SuppressWarnings("unused")
    public String getOpeEndPlanStr() {
        return DateHelper.dateToDDMMYYYY(opeEndPlan);
    }

    @SuppressWarnings("unused")
    public String getOpeEndFactStr() {
        return DateHelper.dateToDDMMYYYY(opeEndFact);
    }

    @SuppressWarnings("unused")
    public Float getLaborRelease() {
        return laborRelease;
    }

    @SuppressWarnings("unused")
    public String getAnaliseEndPlanStr() {
        return DateHelper.dateToDDMMYYYY(analiseEndPlan);
    }

    @SuppressWarnings("unused")
    public String getAnaliseEndFactStr() {
        return DateHelper.dateToDDMMYYYY(analiseEndFact);
    }

    @SuppressWarnings("unused")
    public Float getLaborOPE() {
        return laborOPE;
    }

    @SuppressWarnings("unused")
    public Float getTimePlan() {
        return addTimePlan(laborDevelop) + addTimePlan(laborDebug) + addTimePlan(laborRelease) + addTimePlan(laborOPE);
    }

    @SuppressWarnings("unused")
    public Float getTimeFact() {
        return timeFact;
    }

    @SuppressWarnings("unused")
    public Float getTimeAnalise() {
        return timeAnalise;
    }

    @SuppressWarnings("unused")
    public Float getTimeDevelop() {
        return timeDevelop;
    }

    @SuppressWarnings("unused")
    public Float getTimeDebug() {
        return timeDebug;
    }

    @SuppressWarnings("unused")
    public Float getTimeRelease() {
        return timeRelease;
    }

    @SuppressWarnings("unused")
    public Float getTimeOPE() {
        return timeOPE;
    }

    @SuppressWarnings("unused")
    public Float getTimeWender() {
        return timeWender;
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

    @SuppressWarnings("unused")
    public String getIssuePrototypePlanStr() {
        return DateHelper.dateToDDMMYYYY(issuePrototypePlan);
    }

    @SuppressWarnings("unused")
    public String getIssuePrototypeFactStr() {
        return DateHelper.dateToDDMMYYYY(issuePrototypeFact);
    }

    @SuppressWarnings("unused")
    public Timestamp getStartTaskPlan() {
        return startTaskPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getStartTaskFact() {
        return startTaskFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getAnaliseEndPlan() {
        return analiseEndPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getAnaliseEndFact() {
        return analiseEndFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getDevelopEndPlan() {
        return developEndPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getDevelopEndFact() {
        return developEndFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getDebugEndPlan() {
        return debugEndPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getDebugEndFact() {
        return debugEndFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getIssuingReleaseFact() {
        return issuingReleaseFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getReleaseEndPlan() {
        return releaseEndPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getReleaseEndFact() {
        return releaseEndFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getOpeEndPlan() {
        return opeEndPlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getOpeEndFact() {
        return opeEndFact;
    }

    @SuppressWarnings("unused")
    public Timestamp getIssuePrototypePlan() {
        return issuePrototypePlan;
    }

    @SuppressWarnings("unused")
    public Timestamp getIssuePrototypeFact() {
        return issuePrototypeFact;
    }

    @Override
    public Long getWorkId() {
        return workId;
    }

    @Override
    public Long getProjectId() {
        return projectId;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @SuppressWarnings("unused")
    public String getProjectCode() {
        return projectCode;
    }

    @SuppressWarnings("unused")
    public String getProjectName() {
        return projectName;
    }
}
