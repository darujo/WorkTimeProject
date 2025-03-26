package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "workTime")
public class  WorkTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nikName")
    private String nikName;
    @Column(name = "workDate")
    private Timestamp workDate;
    @Column(name = "workTime")
    private Float workTime;
    @Column(name = "taskId")
    private Long taskId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "type")
    private Integer type;

}
