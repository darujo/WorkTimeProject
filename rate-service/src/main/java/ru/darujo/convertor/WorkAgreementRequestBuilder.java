package ru.darujo.convertor;

import ru.darujo.dto.ratestage.StatusRequest;
import ru.darujo.dto.ratestage.WorkAgreementRequestDto;
import ru.darujo.dto.ratestage.WorkAgreementRequestEditDto;
import ru.darujo.model.WorkAgreementRequest;
import ru.darujo.service.WorkAgreementResponseService;

import java.sql.Timestamp;

public class WorkAgreementRequestBuilder {
    public static WorkAgreementRequestBuilder createWorkAgreementRequest() {
        return new WorkAgreementRequestBuilder();
    }

    private Long id;
    private String nikName;
    private Timestamp timestamp;
    private String version;
    private String comment;
    private Timestamp term;
    private String status;
    private Long workId;

    public WorkAgreementRequestBuilder setId(Long id) {
        this.id = id;
        return this;
    }


    public WorkAgreementRequestBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public WorkAgreementRequestBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public WorkAgreementRequestBuilder setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WorkAgreementRequestBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public WorkAgreementRequestBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public WorkAgreementRequestBuilder setTerm(Timestamp term) {
        this.term = term;
        return this;
    }
    public WorkAgreementRequestBuilder setStatus(StatusRequest status) {
        this.status = status.toString();
        return this;
    }
    public WorkAgreementRequestBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public WorkAgreementRequestEditDto getWorkAgreementRequestEditDto() {
        return new WorkAgreementRequestEditDto(id, nikName, timestamp, version, comment, term, StatusRequest.valueOf(status), workId);
    }

    public WorkAgreementRequest getWorkAgreementRequest() {
        return new WorkAgreementRequest(id, nikName, timestamp, version, comment, term, status, workId);
    }

    public WorkAgreementRequestDto getWorkAgreementRequestDto() {
        return new WorkAgreementRequestDto(
                id,
                nikName,
                timestamp,
                version,
                comment,
                term,
                StatusRequest.valueOf(status),
                workId,

                WorkAgreementResponseService
                        .getINSTANCE()
                        .findWorkAgreementResponse(workId,id)
                        .stream()
                        .map(WorkAgreementResponseConvertor::getWorkAgreementResponseDTO)
                        .toList()
        );
    }
}
