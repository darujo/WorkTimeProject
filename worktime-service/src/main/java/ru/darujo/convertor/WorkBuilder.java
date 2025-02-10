package ru.darujo.convertor;

import ru.darujo.dto.WorkDto;
import ru.darujo.model.Work;


import java.util.Date;

public class WorkBuilder {
    private Long id;
    // Разработка прототипа
    private Date dateStartDevelop;
    // Стабилизация прототипа
    private Date dateStartDebug;
    // Стабилизация релиза
    private Date dateStartRelease;
    // ОПЭ релиза
    private Date dateStartOPE;
    // ВЕНДЕРКА
    private Date dateStartWender;

    public WorkBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public WorkBuilder setDateStartDevelop(Date dateStartDevelop) {
        this.dateStartDevelop = dateStartDevelop;
        return this;
    }
    public WorkBuilder setDateStartDebug(Date dateStartDebug) {
        this.dateStartDebug = dateStartDebug;
        return this;
    }
    public WorkBuilder setDateStartRelease(Date dateStartRelease) {
        this.dateStartRelease = dateStartRelease;
        return this;
    }
    public WorkBuilder setDateStartOPE(Date dateStartOPE) {
        this.dateStartOPE = dateStartOPE;
        return this;
    }
    public WorkBuilder setDateStartWender(Date dateStartWender) {
        this.dateStartWender = dateStartWender;
        return this;
    }

    public static WorkBuilder createWork () {
        return new WorkBuilder();
    }
    public WorkDto getWorkDto(){
        return new WorkDto(id,dateStartDevelop,dateStartDebug,dateStartRelease,dateStartOPE,dateStartWender);
    }
    public Work getWork(){
        return new Work(id,dateStartDevelop, dateStartDebug,dateStartRelease,dateStartOPE,dateStartWender,null);
    }
}
