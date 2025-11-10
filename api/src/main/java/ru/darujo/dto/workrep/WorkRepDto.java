package ru.darujo.dto.workrep;

import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.work.WorkPlanTime;

import java.io.Serializable;
import java.util.Date;

public class WorkRepDto extends DataHelper implements Serializable, WorkPlanTime {
    @SuppressWarnings("unused")
    public WorkRepDto() {
    }

    private Long id;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Дата начала доработки План
    private Date startTaskPlan;
    @SuppressWarnings("unused")
    private String startTaskPlanStr;
    // Дата начала доработки Факт
    private Date startTaskFact;
    @SuppressWarnings("unused")
    private String startTaskFactStr;
    // ВЕНДЕРКА План
    private Date analiseEndPlan;
    @SuppressWarnings("unused")
    private String analiseEndPlanStr;
    // ВЕНДЕРКА
    private Date analiseEndFact;
    @SuppressWarnings("unused")
    private String analiseEndFactStr;
    // Плановые трудозатраты, чел/час анализ
    private Float laborAnalise;
    // Плановые трудозатраты, чел/час разработки
    private Float laborDevelop;
    //начало разработки план
    private Date developEndPlan;
    @SuppressWarnings("unused")
    private String developEndPlanStr;
    //начало разработки факт
    private Date developEndFact;
    @SuppressWarnings("unused")
    private String developEndFactStr;
    // Стабилизация прототипа план
    private Date debugEndPlan;
    @SuppressWarnings("unused")
    private String debugEndPlanStr;
    // Стабилизация прототипа факт
    private Date debugEndFact;
    @SuppressWarnings("unused")
    private String debugEndFactStr;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Date issuingReleasePlan;
    @SuppressWarnings("unused")
    private String issuingReleasePlanStr;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;
    @SuppressWarnings("unused")
    private String issuingReleaseFactStr;
    // Стабилизация релиза
    private Date releaseEndPlan;
    @SuppressWarnings("unused")
    private String releaseEndPlanStr;
    // Стабилизация релиза
    private Date releaseEndFact;
    @SuppressWarnings("unused")
    private String releaseEndFactStr;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // ОПЭ релиза
    private Date opeEndPlan;
    @SuppressWarnings("unused")
    private String opeEndPlanStr;
    // ОПЭ релиза
    private Date opeEndFact;
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
    private Date issuePrototypePlan;
    @SuppressWarnings("unused")
    private String issuePrototypePlanStr;
    private Date issuePrototypeFact;
    @SuppressWarnings("unused")
    private String issuePrototypeFactStr;

    public WorkRepDto(Long id,
                      String codeZI,
                      String name,
                      Date startTaskPlan,
                      Date startTaskFact,
                      Date analiseEndPlan,
                      Date analiseEndFact,
                      Date developEndPlan,
                      Date developEndFact,
                      Date debugEndPlan,
                      Date debugEndFact,
                      String release,
                      Date issuingReleasePlan,
                      Date issuingReleaseFact,
                      Date releaseEndPlan,
                      Date releaseEndFact,
                      Date opeEndPlan,
                      Date opeEndFact,
                      Float timeAnalise,
                      Float timeDevelop,
                      Float timeDebug,
                      Float timeRelease,
                      Float timeOPE,
                      Float timeWender,
                      Date issuePrototypePlan,
                      Date issuePrototypeFact) {
        this.id = id;
        this.codeZI = codeZI;
        this.name = name;
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

    public Float addTimePlan(Float time) {
        if (time != null) {
            return time;
        }
        return 0f;
    }

    public void addTimeFact(Float time) {
        if (time != null) {
            timeFact = timeFact + time;
        }
    }

    public Long getId() {
        return id;
    }

    public String getCodeZI() {
        return codeZI;
    }

    public String getName() {
        return name;
    }

    public String getStartTaskPlanStr() {
        return dateToDDMMYYYY(startTaskPlan);
    }

    public String getStartTaskFactStr() {
        return dateToDDMMYYYY(startTaskFact);
    }

    public String getDevelopEndPlanStr() {
        return dateToDDMMYYYY(developEndPlan);
    }

    public String getDevelopEndFactStr() {
        return dateToDDMMYYYY(developEndFact);
    }

    public Float getLaborAnalise() {
        return laborAnalise;
    }

    public Float getLaborDevelop() {
        return laborDevelop;
    }

    public String getDebugEndPlanStr() {
        return dateToDDMMYYYY(debugEndPlan);
    }

    public String getDebugEndFactStr() {
        return dateToDDMMYYYY(debugEndFact);
    }

    public String getReleaseEndPlanStr() {
        return dateToDDMMYYYY(releaseEndPlan);
    }

    public String getReleaseEndFactStr() {
        return dateToDDMMYYYY(releaseEndFact);
    }

    public Float getLaborDebug() {
        return laborDebug;
    }

    public String getRelease() {
        return release;
    }

    public String getIssuingReleasePlanStr() {
        return dateToDDMMYYYY(issuingReleasePlan);
    }

    public String getIssuingReleaseFactStr() {
        return dateToDDMMYYYY(issuingReleaseFact);
    }

    public String getOpeEndPlanStr() {
        return dateToDDMMYYYY(opeEndPlan);
    }

    public String getOpeEndFactStr() {
        return dateToDDMMYYYY(opeEndFact);
    }

    public Float getLaborRelease() {
        return laborRelease;
    }

    public String getAnaliseEndPlanStr() {
        return dateToDDMMYYYY(analiseEndPlan);
    }

    public String getAnaliseEndFactStr() {
        return dateToDDMMYYYY(analiseEndFact);
    }

    public Float getLaborOPE() {
        return laborOPE;
    }

    public Float getTimePlan() {
        return addTimePlan(laborDevelop) + addTimePlan(laborDebug) + addTimePlan(laborRelease) + addTimePlan(laborOPE);
    }

    public Float getTimeFact() {
        return timeFact;
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
        return dateToDDMMYYYY(issuePrototypePlan);
    }

    @SuppressWarnings("unused")
    public String getIssuePrototypeFactStr() {
        return dateToDDMMYYYY(issuePrototypeFact);
    }

    @SuppressWarnings("unused")
    public Date getStartTaskPlan() {
        return startTaskPlan;
    }

    @SuppressWarnings("unused")
    public Date getStartTaskFact() {
        return startTaskFact;
    }

    @SuppressWarnings("unused")
    public Date getAnaliseEndPlan() {
        return analiseEndPlan;
    }

    @SuppressWarnings("unused")
    public Date getAnaliseEndFact() {
        return analiseEndFact;
    }

    @SuppressWarnings("unused")
    public Date getDevelopEndPlan() {
        return developEndPlan;
    }

    @SuppressWarnings("unused")
    public Date getDevelopEndFact() {
        return developEndFact;
    }

    @SuppressWarnings("unused")
    public Date getDebugEndPlan() {
        return debugEndPlan;
    }

    @SuppressWarnings("unused")
    public Date getDebugEndFact() {
        return debugEndFact;
    }

    @SuppressWarnings("unused")
    public Date getIssuingReleasePlan() {
        return issuingReleasePlan;
    }

    @SuppressWarnings("unused")
    public Date getIssuingReleaseFact() {
        return issuingReleaseFact;
    }

    @SuppressWarnings("unused")
    public Date getReleaseEndPlan() {
        return releaseEndPlan;
    }

    @SuppressWarnings("unused")
    public Date getReleaseEndFact() {
        return releaseEndFact;
    }

    @SuppressWarnings("unused")
    public Date getOpeEndPlan() {
        return opeEndPlan;
    }

    @SuppressWarnings("unused")
    public Date getOpeEndFact() {
        return opeEndFact;
    }

    @SuppressWarnings("unused")
    public Date getIssuePrototypePlan() {
        return issuePrototypePlan;
    }

    @SuppressWarnings("unused")
    public Date getIssuePrototypeFact() {
        return issuePrototypeFact;
    }
}
