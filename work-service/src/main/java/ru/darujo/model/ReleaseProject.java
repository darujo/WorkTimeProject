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
@Table(name = "release_project")
public class ReleaseProject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Выдача релиза дата факт
    @Column(name = "issuing_release_fact")
    private Timestamp issuingReleaseFact;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "release_id", nullable = false)
    private Release release;
    @Column(name = "project_id", nullable = false)
    private Long projectId;

}
