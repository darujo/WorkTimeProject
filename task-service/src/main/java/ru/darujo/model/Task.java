package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    // № запроса (BTS)
    @Column(name = "code_bts")
    private String codeBTS;
    // № внутренней задачи (D E V B O)
    @Column(name = "code")
    private String code;
    // Краткое описание ошибки
    @Column(name = "description")
    private String description;
    // Тип задачи
    @Column(name = "type")
    private  Integer type;
    // № ЗИ (ZI)
    @Column(name = "work_id")
    private  Long workId;
    @Column(name = "refresh")
    private LocalDateTime refresh;
    @Column(name = "time_create")
    private LocalDateTime timeCreate;
    @Column(name = "project_id", nullable = false)
    private Long projectId;
}
