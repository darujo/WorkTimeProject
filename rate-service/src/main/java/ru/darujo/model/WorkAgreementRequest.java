package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "work_agreement_request")
public class WorkAgreementRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "timestamp")
    private LocalDateTime dateTime;
    @Column(name = "version")
    private String version;
    @Column(name = "comment")
    private String comment;
    @Column(name = "term")
    private LocalDate term;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;


}
