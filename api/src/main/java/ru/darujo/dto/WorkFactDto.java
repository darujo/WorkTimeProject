package ru.darujo.dto;

import java.io.Serializable;

public class WorkFactDto implements Serializable {
    public WorkFactDto() {
    }
    private Integer num;
    private String codeZi;
    private String name;
    private Integer userCol;
    private String userName;
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

    public Integer getNum() {
        return num;
    }

    public Integer getUserCol() {
        return userCol;
    }

    public String getCodeZi() {
        return codeZi;
    }

    public String getUserName() {
        return userName;
    }

    public WorkFactDto(Integer num, String codeZi, String name,Integer userCol, String userName, Float timeAnalise, Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender) {
        this.num = num;
        this.codeZi = codeZi;
        this.name = name;
        this.userName = userName;
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



}
