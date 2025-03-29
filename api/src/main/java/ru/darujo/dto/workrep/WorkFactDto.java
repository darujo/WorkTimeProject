package ru.darujo.dto.workrep;

import java.io.Serializable;

public class WorkFactDto implements Serializable {
    public WorkFactDto() {
    }
    private Integer num;
    private String codeZi;
    private String name;
    private Integer userCol;
    private String nikName;
    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public void setAuthorPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

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

    public String getNikName() {
        return nikName;
    }

    public WorkFactDto(Integer num, String codeZi, String name,Integer userCol, String nikName, String authorFirstName,String authorLastName, String authorPatronymic, Float timeAnalise, Float timeDevelop, Float timeDebug, Float timeRelease, Float timeOPE, Float timeWender) {
        this.num = num;
        this.codeZi = codeZi;
        this.name = name;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.nikName = nikName;
        this.userCol = userCol;
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
