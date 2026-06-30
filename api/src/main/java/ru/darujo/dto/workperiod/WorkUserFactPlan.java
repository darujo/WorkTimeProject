package ru.darujo.dto.workperiod;

import ru.darujo.assistant.helper.DateHelper;

import java.time.ZonedDateTime;

public class WorkUserFactPlan extends DateHelper {
    @SuppressWarnings("unused")
    public WorkUserFactPlan() {
    }

    private String nikName;
    private ZonedDateTime dateStart;
    private ZonedDateTime dateEnd;
    private Float timePlan;
    private Float timeFact;

    public WorkUserFactPlan(String nikName, ZonedDateTime dateStart, ZonedDateTime dateEnd, Float timePlan, Float timeFact) {
        this.nikName = nikName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timePlan = timePlan;
        this.timeFact = timeFact;
    }

    @SuppressWarnings("unused")
    public String getNikName() {
        return nikName;
    }

    @SuppressWarnings("unused")
    public ZonedDateTime getDateStart() {
        return dateStart;
    }

    @SuppressWarnings("unused")
    public String getPeriodStr() {
        return dateStart.equals(dateEnd) ?
                dateToDDMMYYYY(dateStart) :
                dateToDDMMYYYY(dateStart) + " - " + dateToDDMMYYYY(dateEnd);
    }

    @SuppressWarnings("unused")
    public ZonedDateTime getDateEnd() {
        return dateEnd;
    }


    @SuppressWarnings("unused")
    public Float getTimePlan() {
        return timePlan;
    }

    @SuppressWarnings("unused")
    public Float getTimeFact() {
        return timeFact;
    }
}
