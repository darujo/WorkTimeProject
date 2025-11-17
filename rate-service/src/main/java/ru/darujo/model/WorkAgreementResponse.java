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
@Table(name = "work_agreement_response")
public class WorkAgreementResponse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nik_name")
    private String nikName;
    @Column(name = "timestamp")
    private Timestamp timestamp;
    @Column(name = "comment")
    private String comment;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;
    @ManyToOne()
    @JoinColumn(name = "request_id")
    private WorkAgreementRequest request;

}
