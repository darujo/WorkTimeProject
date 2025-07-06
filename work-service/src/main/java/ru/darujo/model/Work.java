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
    @Column(name = "code_sap")
    private Long codeSap;
    // Код Зи
    @Column(name = "codeZI")
    private String codeZI;
    // Наименование Зи
    @Column(name = "name")
    private String name;
    // Разработка прототипа Факт
    // ВЕНДЕРКА Факт
    @Column(name = "analise_end_fact")
    private Timestamp analiseEndFact;
    // ВЕНДЕРКА Факт
    @Column(name = "analise_end_plan")
    private Timestamp analiseEndPlan;
    @Column(name = "develop_end_fact")
    private Timestamp developEndFact;
    //начало разработки план
    @Column(name = "develop_end_plan")
    private Timestamp developEndPlan;
    // Стабилизация прототипа Факт
    @Column(name = "debug_end_fact")
    private Timestamp debugEndFact;
    // Стабилизация релиза Факт
    @Column(name = "debug_end_plan")
    private Timestamp debugEndPlan;
    // Стабилизация релиза Факт
    @Column(name = "release_end_fact")
    private Timestamp releaseEndFact;
    // Стабилизация релиза plan
    @Column(name = "release_end_plan")
    private Timestamp releaseEndPlan;
    // ОПЭ релиза Факт
    @Column(name = "ope_end_fact")
    private Timestamp opeEndFact;
    // ОПЭ релиза Факт
    @Column(name = "ope_end_plan")
    private Timestamp opeEndPlan;
    // № внутренней задачи (DEVBO)
    @Column(name = "task")
    private String task;
    // Краткое описание внутренней задачи
    @Column(name = "description")
    private String description;
    // Дата начала доработки План
    @Column(name = "start_task_plan")
    private Timestamp startTaskPlan;
    // Дата начала доработки Факт
    @Column(name = "start_task_fact")
    private Timestamp startTaskFact;
    // Текущий этап ЗИ
    @Column(name = "stageZI")
    private Integer stageZI;
    // Порядковый номер релиза
    @ManyToOne
    @JoinColumn(name="release_id")
    private Release release;

    // ВЕНДЕРКА Факт
    @Column(name = "analise_start_fact")
    private Timestamp analiseStartFact;
    @Column(name = "develop_start_fact")
    private Timestamp developStartFact;
    // Стабилизация прототипа Факт
    @Column(name = "debug_start_fact")
    private Timestamp debugStartFact;
    // Стабилизация релиза Факт
    @Column(name = "release_start_fact")
    private Timestamp releaseStartFact;
    // ОПЭ релиза Факт
    @Column(name = "ope_start_fact")
    private Timestamp opeStartFact;

    // ВЕНДЕРКА Факт
    @Column(name = "analise_start_plan")
    private Timestamp analiseStartPlan;
    //начало разработки план
    @Column(name = "develop_start_plan")
    private Timestamp developStartPlan;
    // Стабилизация релиза Факт
    @Column(name = "debug_start_plan")
    private Timestamp debugStartPlan;
    // Стабилизация релиза plan
    @Column(name = "release_start_plan")
    private Timestamp releaseStartPlan;
    // ОПЭ релиза Факт
    @Column(name = "ope_start_plan")
    private Timestamp opeStartPlan;

}
