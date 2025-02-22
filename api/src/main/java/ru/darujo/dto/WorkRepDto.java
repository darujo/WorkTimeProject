package ru.darujo.dto;

import java.io.Serializable;

public class WorkRepDto implements Serializable {
    private String name;
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

    public WorkRepDto(String name, Float timeAnalise,Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender) {
        this.name = name;
        this.timeAnalise = timeAnalise;
        this.timeDevelop = timeDevelop;
        this.timeDebug = timeDebug;
        this.timeRelease = timeRelease;
        this.timeOPE = timeOPE;
        this.timeWender = timeWender;
    }

    public String getName() {
        return name;
    }

    public Float getTimeAnalise() {return timeAnalise; }

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

    public WorkRepDto() {
    }


}
