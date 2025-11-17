package ru.darujo.convertor;

import ru.darujo.dto.ratestage.WorkAgreementResponseDto;
import ru.darujo.model.WorkAgreementResponse;

public class WorkAgreementResponseConvertor {
    public static WorkAgreementResponseDto getWorkAgreementResponseDTO(WorkAgreementResponse workAgreementResponse){
        return WorkAgreementResponseBuilder
                .create()
                .setId(workAgreementResponse.getId())
                .setNikName(workAgreementResponse.getNikName())
                .setTimestamp(workAgreementResponse.getTimestamp())
                .setWorkId(workAgreementResponse.getWorkId())
                .setComment(workAgreementResponse.getComment())
                .setRequest(workAgreementResponse.getRequest())
                .setStatus(workAgreementResponse.getStatus())
                .getWorkAgreementResponseDTO();
    }
    public static WorkAgreementResponse getWorkAgreementResponse(WorkAgreementResponseDto workAgreementResponse){
        return WorkAgreementResponseBuilder
                .create()
                .setId(workAgreementResponse.getId())
                .setNikName(workAgreementResponse.getNikName())
                .setTimestamp(workAgreementResponse.getTimestamp())
                .setWorkId(workAgreementResponse.getWorkId())
                .setComment(workAgreementResponse.getComment())
                .setRequest(workAgreementResponse.getRequestId())
                .setStatus(workAgreementResponse.getStatus())
                .getWorkAgreementResponse();
    }
}
