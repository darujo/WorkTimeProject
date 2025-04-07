package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = { "chequeLines" })
@Data
@Entity
@Table(name = "work")
public class Work {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Код SAP
    @Column(name = "codeSap")
    private Long codeSap;
    // Код Зи
    @Column(name = "codeZI")
    private String codeZI;
    // Наименование Зи
    @Column(name = "name")
    private String name;
    // Разработка прототипа Факт
    // ВЕНДЕРКА Факт
    @Column(name = "analiseEndFact")
    private Timestamp analiseEndFact;
    // ВЕНДЕРКА Факт
    @Column(name = "analiseEndPlan")
    private Timestamp analiseEndPlan;
    @Column(name = "developEndFact")
    private Timestamp developEndFact;
    //начало разработки план
    @Column(name = "developEndPlan")
    private Timestamp developEndPlan;
    // Стабилизация прототипа Факт
    @Column(name = "debugEndFact")
    private Timestamp debugEndFact;
    // Стабилизация релиза Факт
    @Column(name = "debugEndPlan")
    private Timestamp debugEndPlan;
    // Стабилизация релиза Факт
    @Column(name = "releaseEndFact")
    private Timestamp releaseEndFact;
    // Стабилизация релиза plan
    @Column(name = "releaseEndPlan")
    private Timestamp releaseEndPlan;
    // ОПЭ релиза Факт
    @Column(name = "opeEndFact")
    private Timestamp opeEndFact;
    // ОПЭ релиза Факт
    @Column(name = "opeEndPlan")
    private Timestamp opeEndPlan;
    // № внутренней задачи (DEVBO)
    @Column(name = "task")
    private String task;
    // Краткое описание внутренней задачи
    @Column(name = "description")
    private String description;
    // Дата начала доработки План
    @Column(name = "startTaskPlan")
    private Timestamp startTaskPlan;
    // Дата начала доработки Факт
    @Column(name = "startTaskFact")
    private Timestamp startTaskFact;
    // Текущий этап ЗИ
    @Column(name = "stageZI")
    private Integer stageZI;
    // Порядковый номер релиза
    @Column(name = "release")
    private String release;
    // Выдача релиза даты План
    @Column(name = "issuingReleasePlan")
    private Timestamp issuingReleasePlan;
    // Выдача релиза дата факт
    @Column(name = "issuingReleaseFact")
    private Timestamp issuingReleaseFact;

}
