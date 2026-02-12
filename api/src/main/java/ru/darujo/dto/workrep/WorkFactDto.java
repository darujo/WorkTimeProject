package ru.darujo.dto.workrep;

import java.io.Serializable;

public class WorkFactDto implements Serializable {
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public String getAuthorFirstName() {
        return authorFirstName;
    }

    @SuppressWarnings("unused")
    public String getAuthorLastName() {
        return authorLastName;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public Integer getNum() {
        return num;
    }

    @SuppressWarnings("unused")
    public Integer getUserCol() {
        return userCol;
    }

    @SuppressWarnings("unused")
    public String getCodeZi() {
        return codeZi;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public Float getTimeAnalise() {return timeAnalise; }

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

    public void setUserCol(Integer userCol) {
        this.userCol = userCol;
    }
}
