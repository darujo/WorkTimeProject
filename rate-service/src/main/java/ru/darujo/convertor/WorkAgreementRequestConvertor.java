package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkAgreementRequestDto;
import ru.darujo.dto.ratestage.WorkAgreementRequestEditDto;
import ru.darujo.model.WorkAgreementRequest;

public class WorkAgreementRequestConvertor {
    public static WorkAgreementRequestEditDto getWorkAgreementRequestEditDto(WorkAgreementRequest workAgreementRequest){
        return WorkAgreementRequestBuilder
                .createWorkAgreementRequest()
                .setId(workAgreementRequest.getId())
                .setComment(workAgreementRequest.getComment())
                .setNikName(workAgreementRequest.getNikName())
                .setTimestamp(workAgreementRequest.getTimestamp())
                .setVersion(workAgreementRequest.getVersion())
                .setTerm(workAgreementRequest.getTerm())
                .setStatus(workAgreementRequest.getStatus())
                .setWorkId(workAgreementRequest.getWorkId())
                .getWorkAgreementRequestEditDto();
    }

    public static WorkAgreementRequest getWorkAgreementRequest(WorkAgreementRequestEditDto workAgreementRequest) {
        return WorkAgreementRequestBuilder
                .createWorkAgreementRequest()
                .setId(workAgreementRequest.getId())
                .setComment(workAgreementRequest.getComment())
                .setNikName(workAgreementRequest.getNikName())
                .setTimestamp(workAgreementRequest.getTimestamp())
                .setVersion(workAgreementRequest.getVersion())
                .setTerm(workAgreementRequest.getTerm())
                .setStatus(workAgreementRequest.getStatus())
                .setWorkId(workAgreementRequest.getWorkId())
                .getWorkAgreementRequest();
    }
    public static WorkAgreementRequestDto getWorkAgreementRequestDto(WorkAgreementRequest workAgreementRequest){
        return WorkAgreementRequestBuilder
                .createWorkAgreementRequest()
                .setId(workAgreementRequest.getId())
                .setComment(workAgreementRequest.getComment())
                .setNikName(workAgreementRequest.getNikName())
                .setTimestamp(workAgreementRequest.getTimestamp())
                .setVersion(workAgreementRequest.getVersion())
                .setTerm(workAgreementRequest.getTerm())
                .setStatus(workAgreementRequest.getStatus())
                .setWorkId(workAgreementRequest.getWorkId())
                .getWorkAgreementRequestDto();
    }
}
