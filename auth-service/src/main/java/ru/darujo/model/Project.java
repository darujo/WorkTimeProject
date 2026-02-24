package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "stage_end", nullable = false)
    private Integer stageEnd;

    @Column(name = "stage_0_name")
    private String stage0Name;
    @Column(name = "stage_1_name")
    private String stage1Name;
    @Column(name = "stage_2_name")
    private String stage2Name;
    @Column(name = "stage_3_name")
    private String stage3Name;
    @Column(name = "stage_4_name")
    private String stage4Name;

}
