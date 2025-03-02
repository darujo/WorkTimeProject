package ru.darujo.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkRepDto implements Serializable {
    public WorkRepDto() {
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
    private Long id;
    // Код Зи
    private String codeZI;
    // Наименование
    private String name;
    // Плановая дата завершения 0 этапа
    private Date planDateStage0;
    // Плановая дата завершения 0 этапа
    private Date factDateStage0;

    // Дата начала доработки План
    private Date startTaskPlan;
    // Дата начала доработки Факт
    private Date startTaskFact;
    //начало разработки план
    private Date dateStartDevelopPlan;
    //начало разработки факт
    private Date dateStartDevelop;
    // Плановые трудозатраты, чел/час разработки
    private Float laborDevelop;
    // Стабилизация прототипа план
    private Date dateStartDebugPlan;
    // Стабилизация прототипа факт
    private Date dateStartDebug;
    // Стабилизация релиза
    private Date dateStartReleasePlan;
    // Стабилизация релиза
    private Date dateStartRelease;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;
    // ОПЭ релиза
    private Date dateStartOPEPlan;
    // ОПЭ релиза
    private Date dateStartOPE;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // ВЕНДЕРКА План
    private Date dateStartWenderPlan;
    // ВЕНДЕРКА
    private Date dateStartWender;
    // Плановые трудозатраты, чел/час ОПЭ
    private Float laborOPE;
    // плановое время
    private Float timePlan;
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
    public WorkRepDto(Long id,String codeZI, String name, Date planDateStage0, Date factDateStage0, Date startTaskPlan, Date startTaskFact, Date dateStartDevelopPlan, Date dateStartDevelop, Float laborDevelop, Date dateStartDebugPlan, Date dateStartDebug, Date dateStartReleasePlan, Date dateStartRelease, Float laborDebug, String release, Date issuingReleasePlan, Date issuingReleaseFact, Date dateStartOPEPlan, Date dateStartOPE, Float laborRelease, Date dateStartWenderPlan, Date dateStartWender, Float laborOPE, Float timeAnalise, Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender) {
        this.id = id;
        this.codeZI = codeZI;
        this.name = name;
        this.planDateStage0 = planDateStage0;
        this.factDateStage0 = factDateStage0;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.dateStartDevelopPlan = dateStartDevelopPlan;
        this.dateStartDevelop = dateStartDevelop;
        this.laborDevelop = laborDevelop;
        this.dateStartDebugPlan = dateStartDebugPlan;
        this.dateStartDebug = dateStartDebug;
        this.dateStartReleasePlan = dateStartReleasePlan;
        this.dateStartRelease = dateStartRelease;
        this.laborDebug = laborDebug;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.dateStartOPEPlan = dateStartOPEPlan;
        this.dateStartOPE = dateStartOPE;
        this.laborRelease = laborRelease;
        this.dateStartWenderPlan = dateStartWenderPlan;
        this.dateStartWender = dateStartWender;
        this.laborOPE = laborOPE;
        this.timeAnalise = timeAnalise;
        this.timeDevelop = timeDevelop;
        this.timeDebug = timeDebug;
        this.timeRelease = timeRelease;
        this.timeOPE = timeOPE;
        this.timeWender = timeWender;
        this.timePlan = 0f;
        addTimePlan(laborDevelop);
        addTimePlan(laborDebug);
        addTimePlan(laborRelease);
        addTimePlan(laborOPE);
        this.timeFact = 0f;
        addTimeFact(timeAnalise);
        addTimeFact(timeDevelop);
        addTimeFact(timeDebug);
        addTimeFact(timeRelease);
        addTimeFact(timeOPE);
    }

    public void addTimePlan(Float time ){
        if (time != null){
            timePlan = timePlan +time;
        }
    }
    public void addTimeFact(Float time ){
        if (time != null){
            timeFact = timeFact +time;
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

    public String getPlanDateStage0() {
        return dateToText(planDateStage0);
    }

    public String getFactDateStage0() {
        return dateToText(factDateStage0);
    }

    public String getStartTaskPlan() {
        return dateToText(startTaskPlan);
    }

    public String getStartTaskFact() {
        return dateToText(startTaskFact);
    }

    public String getDateStartDevelopPlan() {
        return dateToText(dateStartDevelopPlan);
    }

    public String getDateStartDevelop() {
        return dateToText(dateStartDevelop);
    }

    public Float getLaborDevelop() {
        return laborDevelop;
    }

    public String getDateStartDebugPlan() {
        return dateToText(dateStartDebugPlan);
    }

    public String getDateStartDebug() {
        return dateToText(dateStartDebug);
    }

    public String getDateStartReleasePlan() {
        return dateToText(dateStartReleasePlan);
    }

    public String getDateStartRelease() {
        return dateToText(dateStartRelease);
    }

    public Float getLaborDebug() {
        return laborDebug;
    }

    public String getRelease() {
        return release;
    }

    public String getIssuingReleasePlan() {
        return dateToText(issuingReleasePlan);
    }

    public String getIssuingReleaseFact() {
        return dateToText(issuingReleaseFact);
    }

    public String getDateStartOPEPlan() {
        return dateToText(dateStartOPEPlan);
    }

    public String getDateStartOPE() {
        return dateToText(dateStartOPE);
    }

    public Float getLaborRelease() {
        return laborRelease;
    }

    public String getDateStartWenderPlan() {
        return dateToText(dateStartWenderPlan);
    }

    public String getDateStartWender() {
        return dateToText(dateStartWender);
    }

    public Float getLaborOPE() {
        return laborOPE;
    }

    public Float getTimePlan() {
        return timePlan;
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
}
