package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @ManyToOne
    @JoinColumn(name="release_id")
    private Release release;

    // ВЕНДЕРКА Факт
    @Column(name = "analiseStartFact")
    private Timestamp analiseStartFact;
    @Column(name = "developStartFact")
    private Timestamp developStartFact;
    // Стабилизация прототипа Факт
    @Column(name = "debugStartFact")
    private Timestamp debugStartFact;
    // Стабилизация релиза Факт
    @Column(name = "releaseStartFact")
    private Timestamp releaseStartFact;
    // ОПЭ релиза Факт
    @Column(name = "opeStartFact")
    private Timestamp opeStartFact;

    // ВЕНДЕРКА Факт
    @Column(name = "analiseStartPlan")
    private Timestamp analiseStartPlan;
    //начало разработки план
    @Column(name = "developStartPlan")
    private Timestamp developStartPlan;
    // Стабилизация релиза Факт
    @Column(name = "debugStartPlan")
    private Timestamp debugStartPlan;
    // Стабилизация релиза plan
    @Column(name = "releaseStartPlan")
    private Timestamp releaseStartPlan;
    // ОПЭ релиза Факт
    @Column(name = "opeStartPlan")
    private Timestamp opeStartPlan;

}
