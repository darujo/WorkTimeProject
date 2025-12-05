package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_time")
public class  WorkTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "work_date")
    private Timestamp workDate;
    @Column(name = "work_time")
    private Float workTime;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "type")
    private Integer type;

}
