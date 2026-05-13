package ru.darujo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime dateTime;
    @Column(name = "comment")
    private String comment;
    @Column(name = "status")
    private String status;
    @Column(name = "work_id")
    private Long workId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private WorkAgreementRequest request;
}
