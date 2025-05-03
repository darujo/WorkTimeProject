package ru.darujo.dto.work;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReleaseDto {
    private Long id;
    // Порядковый номер релиза
    private String name;
    // Выдача релиза даты План
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    private Timestamp issuingReleaseFact;

    public ReleaseDto(Long id, String name, Timestamp issuingReleasePlan, Timestamp issuingReleaseFact) {
        this.id = id;
        this.name = name;
        this.issuingReleasePlan = issuingReleasePlan;
        this.issuingReleaseFact = issuingReleaseFact;
    }

    public ReleaseDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getIssuingReleasePlan() {
        return issuingReleasePlan;
    }
    public String getIssuingReleasePlanStr() {
        return dateToText(issuingReleasePlan);
    }

    public Timestamp getIssuingReleaseFact() {
        return issuingReleaseFact;
    }
    public String getIssuingReleaseFactStr() {
        return dateToText(issuingReleaseFact);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
}
