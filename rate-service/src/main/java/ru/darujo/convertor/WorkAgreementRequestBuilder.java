package ru.darujo.convertor;

import ru.darujo.assistant.helper.DateHelper;
import ru.darujo.dto.ratestage.StatusRequest;
import ru.darujo.dto.ratestage.WorkAgreementRequestDto;
import ru.darujo.dto.ratestage.WorkAgreementRequestEditDto;
import ru.darujo.integration.UserServiceIntegrationImp;
import ru.darujo.model.WorkAgreementRequest;
import ru.darujo.service.WorkAgreementResponseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class WorkAgreementRequestBuilder {
    public static WorkAgreementRequestBuilder createWorkAgreementRequest() {
        return new WorkAgreementRequestBuilder();
    }

    private Long id;
    private String nikName;
    private LocalDateTime timestamp;
    private String version;
    private String comment;
    private LocalDate term;
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

    public WorkAgreementRequestBuilder setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WorkAgreementRequestBuilder setTimestamp(ZonedDateTime zonedDateTime) {
        this.timestamp = DateHelper.zDTToLDT(zonedDateTime);
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

    public WorkAgreementRequestBuilder setTerm(LocalDate term) {
        this.term = term;
        return this;
    }

    public WorkAgreementRequestBuilder setTerm(ZonedDateTime zonedDateTime) {
        this.term = DateHelper.zDTToLD(zonedDateTime);
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
        return new WorkAgreementRequestEditDto(id, nikName, DateHelper.getZDT(timestamp), version, comment, DateHelper.getZDT(term), StatusRequest.valueOf(status), workId);
    }

    public WorkAgreementRequest getWorkAgreementRequest() {
        return new WorkAgreementRequest(id, nikName, timestamp, version, comment, term, status, workId);
    }

    public WorkAgreementRequestDto getWorkAgreementRequestDto() {
        WorkAgreementRequestDto workAgreementRequestDto =new WorkAgreementRequestDto(
                id,
                nikName,
                DateHelper.getZDT(timestamp),
                version,
                comment,
                DateHelper.getZDT(term),
                StatusRequest.valueOf(status),
                workId,

                WorkAgreementResponseService
                        .getINSTANCE()
                        .findWorkAgreementResponse(workId,id)
                        .stream()
                        .map(WorkAgreementResponseConvertor::getWorkAgreementResponseDTO)
                        .toList()
        );
        UserServiceIntegrationImp.getInstance().updFio(workAgreementRequestDto);
        return workAgreementRequestDto;
    }
}
