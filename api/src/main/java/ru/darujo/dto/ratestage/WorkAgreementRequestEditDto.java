package ru.darujo.dto.ratestage;

import java.sql.Timestamp;

public class WorkAgreementRequestEditDto {
    @SuppressWarnings("unused")
    public WorkAgreementRequestEditDto() {
    }

    private Long id;
    private String nikName;
    private Timestamp timestamp;
    private String version;
    private String comment;
    private Timestamp term;
    private StatusRequest status;
    private Long workId;

    public WorkAgreementRequestEditDto(Long id, String nikName, Timestamp timestamp, String version, String comment, Timestamp term, StatusRequest status, Long workId) {
        this.id = id;
        this.nikName = nikName;
        this.timestamp = timestamp;
        this.version = version;
        this.comment = comment;
        this.term = term;
        this.status = status;
        this.workId = workId;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getNikName() {
        return nikName;
    }

    @SuppressWarnings("unused")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public String getVersion() {
        return version;
    }

    @SuppressWarnings("unused")
    public String getComment() {
        return comment;
    }

    @SuppressWarnings("unused")
    public Timestamp getTerm() {
        return term;
    }

    @SuppressWarnings("unused")
    public StatusRequest getStatus() {
        return status;
    }
    @SuppressWarnings("unused")
    public String getStatusName() {
        return status.getName();
    }
    @SuppressWarnings("unused")
    public Long getWorkId() {
        return workId;
    }
}
