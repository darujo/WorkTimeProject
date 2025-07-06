package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_stage")
public class WorkStage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "role")
    private Integer role;
    @Column(name = "stage0")
    private Float stage0;
    @Column(name = "stage1")
    private Float stage1;
    @Column(name = "stage2")
    private Float stage2;
    @Column(name = "stage3")
    private Float stage3;
    @Column(name = "stage4")
    private Float stage4;
    @Column(name = "work_id")
    private Long workId;

}
