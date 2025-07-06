package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_criteria")
public class WorkCriteria {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "criteria")
    private Integer criteria;
    @Column(name = "develop10")
    private Float develop10;
    @Column(name = "develop50")
    private Float develop50;
    @Column(name = "develop100")
    private Float develop100;
    @Column(name = "work_id")
    private Long workId;

}
