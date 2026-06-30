package ru.darujo.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SaveDateDevelopEndFact {
    private boolean save;
    private LocalDate date;


    public SaveDateDevelopEndFact setSave(boolean save) {
        this.save = save;
        return this;
    }

    public SaveDateDevelopEndFact setDate(LocalDate date) {
        this.date = date;
        return this;
    }
}