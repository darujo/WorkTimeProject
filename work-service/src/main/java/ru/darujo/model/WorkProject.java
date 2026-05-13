package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    // Анализ Факт
    @Column(name = "analise_end_fact")
    private LocalDate analiseEndFact;
    // Анализ Факт
    @Column(name = "analise_end_plan")
    private LocalDate analiseEndPlan;
    @Column(name = "develop_end_fact")
    private LocalDate developEndFact;
    //начало разработки план
    @Column(name = "develop_end_plan")
    private LocalDate developEndPlan;

    @Column(name = "issue_prototype_fact")
    private LocalDate issuePrototypeFact;
    //Выдача прототипа
    @Column(name = "issue_prototype_plan")
    private LocalDate issuePrototypePlan;
    // Стабилизация прототипа Факт
    @Column(name = "debug_end_fact")
    private LocalDate debugEndFact;
    // Стабилизация релиза Факт
    @Column(name = "debug_end_plan")
    private LocalDate debugEndPlan;
    // Стабилизация релиза Факт
    @Column(name = "release_end_fact")
    private LocalDate releaseEndFact;
    // Стабилизация релиза plan
    @Column(name = "release_end_plan")
    private LocalDate releaseEndPlan;
    // ОПЭ релиза Факт
    @Column(name = "ope_end_fact")
    private LocalDate opeEndFact;
    // ОПЭ релиза Факт
    @Column(name = "ope_end_plan")
    private LocalDate opeEndPlan;
    // Анализ Факт
    @Column(name = "analise_start_fact")
    private LocalDate analiseStartFact;
    @Column(name = "develop_start_fact")
    private LocalDate developStartFact;
    // Стабилизация прототипа Факт
    @Column(name = "debug_start_fact")
    private LocalDate debugStartFact;
    // Стабилизация релиза Факт
    @Column(name = "release_start_fact")
    private LocalDate releaseStartFact;
    // ОПЭ релиза Факт
    @Column(name = "ope_start_fact")
    private LocalDate opeStartFact;

    // Анализ план
    @Column(name = "analise_start_plan")
    private LocalDate analiseStartPlan;
    //начало разработки план
    @Column(name = "develop_start_plan")
    private LocalDate developStartPlan;
    // Стабилизация релиза план
    @Column(name = "debug_start_plan")
    private LocalDate debugStartPlan;
    // Стабилизация релиза план
    @Column(name = "release_start_plan")
    private LocalDate releaseStartPlan;
    // ОПЭ релиза план
    @Column(name = "ope_start_plan")
    private LocalDate opeStartPlan;
    @Column(name = "rated")
    private Boolean rated;
    // Дата начала доработки План
    @Column(name = "start_task_plan")
    private LocalDate startTaskPlan;
    // Дата начала доработки Факт
    @Column(name = "start_task_fact")
    private LocalDate startTaskFact;
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
