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
@Table(name = "release")
public class Release {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Порядковый номер релиза
    @Column(name = "name")
    private String name;
    // Выдача релиза даты План
    @Column(name = "issuing_release_plan")
    private Timestamp issuingReleasePlan;

    @Column(name = "sort")
    private Float sort;


    public Release(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
