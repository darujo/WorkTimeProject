package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "work_project")
public class WorkProject implements WorkProjectInter {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "project_id")
    private Long projectId;
    @ManyToOne
    @JoinColumn(name = "work_id")
    private Work work;
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

    @Column(name = "issue_prototype_fact")
    private Timestamp issuePrototypeFact;
    //Выдача прототипа
    @Column(name = "issue_prototype_plan")
    private Timestamp issuePrototypePlan;
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
    @Column(name = "rated")
    private Boolean rated;
    @ManyToOne
    @JoinColumn(name = "release_id")
    private Release release;
    // Дата начала доработки План
    @Column(name = "start_task_plan")
    private Timestamp startTaskPlan;
    // Дата начала доработки Факт
    @Column(name = "start_task_fact")
    private Timestamp startTaskFact;
    // № внутренней задачи (D E V B O)
    @Column(name = "task")
    private String task;
    // Текущий этап ЗИ
    @Column(name = "stage_zi")
    private Integer stageZi;

    public WorkProject(Long projectId, Work work, Integer stageZi) {
        this.projectId = projectId;
        this.work = work;
        this.stageZi = stageZi;
    }

}
