package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

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
    @Column(name = "codeBTS")
    private String codeBTS;
    // № внутренней задачи (D E V B O)
    @Column(name = "codeDEVBO")
    private String codeDEVBO;
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
    private Timestamp refresh;
    @Column(name = "time_create")
    private Timestamp timeCreate;
}
