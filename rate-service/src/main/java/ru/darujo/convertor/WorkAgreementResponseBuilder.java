package ru.darujo.convertor;

import ru.darujo.dto.ratestage.StatusResponse;
import ru.darujo.dto.ratestage.WorkAgreementResponseDto;
import ru.darujo.model.WorkAgreementRequest;
import ru.darujo.model.WorkAgreementResponse;
import ru.darujo.service.WorkAgreementRequestService;

import java.sql.Timestamp;

public class WorkAgreementResponseBuilder {
    public static WorkAgreementResponseBuilder create() {
        return new WorkAgreementResponseBuilder();
    }

    private Long id;
    private String nikName;
    private Timestamp timestamp;
    private String comment;
    private String status;
    private Long workId;
    private Long requestId;

    public WorkAgreementResponseBuilder setId(Long id) {
        this.id = id;
        return this;
    }


    public WorkAgreementResponseBuilder setWorkId(Long workId) {
        this.workId = workId;
        return this;
    }

    public WorkAgreementResponseBuilder setNikName(String nikName) {
        this.nikName = nikName;
        return this;
    }

    public WorkAgreementResponseBuilder setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WorkAgreementResponseBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public WorkAgreementResponseBuilder setRequest(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public WorkAgreementResponseBuilder setRequest(WorkAgreementRequest request) {
        this.requestId = request.getId();
        return this;
    }
    public WorkAgreementResponseBuilder setStatus(StatusResponse status) {
        this.status = status.toString();
        return this;
    }

    public WorkAgreementResponseBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public WorkAgreementResponseDto getWorkAgreementResponseDTO() {
        return new WorkAgreementResponseDto(id, nikName, timestamp, comment, StatusResponse.valueOf(status), workId, requestId);
    }

    public WorkAgreementResponse getWorkAgreementResponse() {
        return new WorkAgreementResponse(id, nikName, timestamp, comment, status, workId, WorkAgreementRequestService.getInstance().findRequest(id));
    }
}
