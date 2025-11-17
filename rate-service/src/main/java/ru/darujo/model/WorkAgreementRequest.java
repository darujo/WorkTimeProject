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
@Table(name = "work_agreement_request")
public class WorkAgreementRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "version")
    private String version;
    @Column(name = "comment")
    private String comment;
    @Column(name = "term")
    private Timestamp term;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;

}
