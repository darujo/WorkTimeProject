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
    // Выдача релиза дата факт
    @Column(name = "issuing_release_fact")
    private Timestamp issuingReleaseFact;

}
