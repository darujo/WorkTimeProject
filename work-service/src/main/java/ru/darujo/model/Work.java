package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Integer codeSap;
    // Код Зи
    @Column(name = "codeZI")
    private String codeZI;
    // Наименование Зи
    @Column(name = "name")
    private String name;
    // Разработка прототипа Факт
    @Column(name = "startDevelop")
    private Date dateStartDevelop;
    //начало разработки план
    @Column(name = "startDevelopPlan")
    private Date dateStartDevelopPlan;
    // Стабилизация прототипа Факт
    @Column(name = "startDebug")
    private Date dateStartDebug;
    // Стабилизация релиза Факт
    @Column(name = "startDebugPlan")
    private Date dateStartDebugPlan;
    // Стабилизация релиза Факт
    @Column(name = "startRelease")
    private Date dateStartRelease;
    // Стабилизация релиза plan
    @Column(name = "startReleasePlan")
    private Date dateStartReleasePlan;
    // ОПЭ релиза Факт
    @Column(name = "startOPE")
    private Date dateStartOPE;
    // ОПЭ релиза Факт
    @Column(name = "startOPEPlan")
    private Date dateStartOPEPlan;
    // ВЕНДЕРКА Факт
    @Column(name = "startWender")
    private Date dateStartWender;
    // ВЕНДЕРКА Факт
    @Column(name = "startWenderPlan")
    private Date dateStartWenderPlan;
    // № внутренней задачи (DEVBO)
    @Column(name = "task")
    private String task;
    // Краткое описание внутренней задачи
    @Column(name = "description")
    private String description;
    // Плановая дата завершения 0 этапа
    @Column(name = "planDateStage0")
    private Date planDateStage0;
    // Плановая дата завершения 0 этапа
    @Column(name = "factDateStage0")
    private Date factDateStage0;
    // Дата начала доработки План
    @Column(name = "startTaskPlan")
    private Date startTaskPlan;
    // Дата начала доработки Факт
    @Column(name = "startTaskFact")
    private Date startTaskFact;
    // Плановые трудозатраты, чел/час Разработка прототипа
    @Column(name = "laborDevelop")
    private Float laborDevelop;
    // Плановые трудозатраты, чел/час Стабилизация прототипа
    @Column(name = "laborDebug")
    private Float laborDebug;
    // Плановые трудозатраты, чел/час Стабилизация релиза
    @Column(name = "laborRelease")
    private Float laborRelease;
    // Плановые трудозатраты, чел/час ОПЭ
    @Column(name = "laborOPE")
    private Float laborOPE;
    // Текущий этап ЗИ
    @Column(name = "stageZI")
    private Integer stageZI;
    // Порядковый номер релиза
    @Column(name = "release")
    private String release;
    // Выдача релиза даты План
    @Column(name = "issuingReleasePlan")
    private Date issuingReleasePlan;
    // Выдача релиза дата факт
    @Column(name = "issuingReleaseFact")
    private Date issuingReleaseFact;

}
