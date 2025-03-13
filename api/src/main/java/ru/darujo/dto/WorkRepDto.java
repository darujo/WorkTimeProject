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
    // Дата начала доработки План
    private Date startTaskPlan;
    // Дата начала доработки Факт
    private Date startTaskFact;
    // ВЕНДЕРКА План
    private Date analiseEndPlan;
    // ВЕНДЕРКА
    private Date analiseEndFact;
    // Плановые трудозатраты, чел/час разработки
    private Float laborDevelop;
    //начало разработки план
    private Date developEndPlan;
    //начало разработки факт
    private Date developEndFact;
    // Стабилизация прототипа план
    private Date debugEndPlan;
    // Стабилизация прототипа факт
    private Date debugEndFact;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    private Float laborDebug;
    // Порядковый номер релиза
    private String release;
    // Выдача релиза даты План
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    private Date issuingReleaseFact;
    // Стабилизация релиза
    private Date releaseEndPlan;
    // Стабилизация релиза
    private Date releaseEndFact;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    private Float laborRelease;
    // ОПЭ релиза
    private Date opeEndPlan;
    // ОПЭ релиза
    private Date opeEndFact;
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

    public WorkRepDto(Long id, String codeZI, String name, Date startTaskPlan, Date startTaskFact, Date analiseEndPlan, Date analiseEndFact, Float laborDevelop, Date developEndPlan, Date developEndFact, Date debugEndPlan, Date debugEndFact, Float laborDebug, String release, Date issuingReleasePlan, Date issuingReleaseFact, Date releaseEndPlan, Date releaseEndFact, Float laborRelease, Date opeEndPlan, Date opeEndFact, Float laborOPE, Float timeAnalise, Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender) {
        this.id = id;
        this.codeZI = codeZI;
        this.name = name;
        this.startTaskPlan = startTaskPlan;
        this.startTaskFact = startTaskFact;
        this.analiseEndPlan = analiseEndPlan;
        this.analiseEndFact = analiseEndFact;
        this.laborDevelop = laborDevelop;
        this.developEndPlan = developEndPlan;
        this.developEndFact = developEndFact;
        this.debugEndPlan = debugEndPlan;
        this.debugEndFact = debugEndFact;
        this.laborDebug = laborDebug;
        this.release = release;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
        this.releaseEndPlan = releaseEndPlan;
        this.releaseEndFact = releaseEndFact;
        this.laborRelease = laborRelease;
        this.opeEndPlan = opeEndPlan;
        this.opeEndFact = opeEndFact;
        this.laborOPE = laborOPE;
        this.timeAnalise = timeAnalise;
        this.timeDevelop = timeDevelop;
        this.timeDebug = timeDebug;
        this.timeRelease = timeRelease;
        this.timeOPE = timeOPE;
        this.timeWender = timeWender;
        this.timePlan = 0f;
        addTimePlan (laborDevelop);
        addTimePlan (laborDebug);
        addTimePlan (laborRelease);
        addTimePlan (laborOPE);
        this.timeFact = 0f;
        addTimeFact(timeAnalise);
        addTimeFact(timeDevelop);
        addTimeFact(timeDebug);
        addTimeFact(timeRelease);
        addTimeFact(timeOPE);
        addTimeFact(timeWender);


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

    public String getStartTaskPlan() {
        return dateToText(startTaskPlan);
    }

    public String getStartTaskFact() {
        return dateToText(startTaskFact);
    }

    public String getDevelopEndPlan() {
        return dateToText(developEndPlan);
    }

    public String getDevelopEndFact() {
        return dateToText(developEndFact);
    }

    public Float getLaborDevelop() {
        return laborDevelop;
    }

    public String getDebugEndPlan() {
        return dateToText(debugEndPlan);
    }

    public String getDebugEndFact() {
        return dateToText(debugEndFact);
    }

    public String getReleaseEndPlan() {
        return dateToText(releaseEndPlan);
    }

    public String getReleaseEndFact() {
        return dateToText(releaseEndFact);
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

    public String getOpeEndPlan() {
        return dateToText(opeEndPlan);
    }

    public String getOpeEndFact() {
        return dateToText(opeEndFact);
    }

    public Float getLaborRelease() {
        return laborRelease;
    }

    public String getAnaliseEndPlan() {
        return dateToText(analiseEndPlan);
    }

    public String getAnaliseEndFact() {
        return dateToText(analiseEndFact);
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
