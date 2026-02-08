package ru.darujo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "work_project")
public class WorkProjectLittle implements WorkProjectInter {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "project_id")
    private Long projectId;
    @ManyToOne
    @JoinColumn(name = "work_id")
    private WorkLittle workLittle;
    @Column(name = "rated")
    private Boolean rated;
    @ManyToOne
    @JoinColumn(name = "release_id")
    private Release release;
    @Column(name = "stage_zi")
    private Integer stageZi;
    @Column(name = "task")
    private String task;

    public WorkProjectLittle(Long projectId, WorkLittle workLittle) {
        this.projectId = projectId;
        this.workLittle = workLittle;
    }
}
