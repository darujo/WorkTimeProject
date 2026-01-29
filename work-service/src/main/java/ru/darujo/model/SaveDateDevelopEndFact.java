package ru.darujo.model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class SaveDateDevelopEndFact {
    private boolean save;
    private Timestamp date;


    public SaveDateDevelopEndFact setSave(boolean save) {
        this.save = save;
        return this;
    }

    public SaveDateDevelopEndFact setDate(Timestamp date) {
        this.date = date;
        return this;
    }
}