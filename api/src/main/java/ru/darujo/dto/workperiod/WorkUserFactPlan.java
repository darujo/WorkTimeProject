package ru.darujo.dto.workperiod;

import ru.darujo.assistant.helper.DataHelper;

import java.sql.Timestamp;

public class WorkUserFactPlan extends DataHelper {
    @SuppressWarnings("unused")
    public WorkUserFactPlan() {
    }

    private String nikName;
    private Timestamp dateStart;
    private Timestamp dateEnd;
    private Float timePlan;
    private Float timeFact;

    public WorkUserFactPlan(String nikName, Timestamp dateStart, Timestamp dateEnd, Float timePlan, Float timeFact) {
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
    public Timestamp getDateStart() {
        return dateStart;
    }

    @SuppressWarnings("unused")
    public String getPeriodStr() {
        return dateStart.equals(dateEnd) ?
                dateToDDMMYYYY(dateStart) :
                dateToDDMMYYYY(dateStart) + " - " + dateToDDMMYYYY(dateEnd);
    }

    @SuppressWarnings("unused")
    public Timestamp getDateEnd() {
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
