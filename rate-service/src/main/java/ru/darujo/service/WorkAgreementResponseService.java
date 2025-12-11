package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.information.MessageInfoDto;
import ru.darujo.dto.information.MessageType;
import ru.darujo.dto.work.WorkLittleDto;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.InfoServiceIntegration;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkAgreementRequest;
import ru.darujo.model.WorkAgreementResponse;
import ru.darujo.repository.WorkAgreementResponseRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.url.UrlWorkTime;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Primary
public class WorkAgreementResponseService {
    private WorkAgreementRequestService workAgreementRequestService;

    private WorkAgreementResponseRepository workAgreementResponseRepository;
    private InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }

    @Autowired
    public void setWorkAgreementResponseRepository(WorkAgreementResponseRepository workAgreementResponseRepository) {
        this.workAgreementResponseRepository = workAgreementResponseRepository;
    }

    @Getter
    private static WorkAgreementResponseService INSTANCE;

    public WorkAgreementResponseService() {
        INSTANCE = this;
    }

    @PostConstruct
    public void init() {
        workAgreementRequestService = WorkAgreementRequestService.getInstance();
    }

    public Optional<WorkAgreementResponse> findById(long id) {
        return workAgreementResponseRepository.findById(id);
    }

    private void validWorkAgreementResponse(WorkAgreementResponse workAgreementResponse) {
        if (workAgreementResponse.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        if (workAgreementResponse.getRequest() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к запросу");
        }
        Specification<@NonNull WorkAgreementResponse> specification = Specification.where(Specifications.eq(null, "workId", workAgreementResponse.getWorkId()));
        specification = Specifications.eq(specification, "request", workAgreementResponse.getRequest());
        specification = Specifications.eq(specification, "nikName", workAgreementResponse.getNikName());
        specification = Specifications.ne(specification, "id", workAgreementResponse.getId());
        WorkAgreementResponse workAgreementResponseSave = workAgreementResponseRepository.findOne(specification).orElse(null);
        if (workAgreementResponseSave != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с вашим согласованием");
        }

    }

    public WorkAgreementResponse saveWorkAgreementResponse(String author, WorkAgreementResponse workAgreementResponse) {
        WorkAgreementResponse workAgreementResponseSave = null;
        if (workAgreementResponse.getId() != null) {
            workAgreementResponseSave = findById(workAgreementResponse.getId()).orElse(null);
        }
        validWorkAgreementResponse(workAgreementResponse);
        workAgreementResponse = workAgreementResponseRepository.save(workAgreementResponse);
        WorkLittleDto workLittleDto = workServiceIntegration.getWorEditDto(workAgreementResponse.getWorkId());
        try {


            infoServiceIntegration.addMessage(
                    new MessageInfoDto(
                            author,
                            MessageType.EDIT_WORK_REQUEST,
                            workAgreementResponseSave == null
                                    ?
                                    ("Добавлен согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workLittleDto) + " по версии ТЗ " + workAgreementResponse.getRequest().getVersion() + "\n" + workAgreementResponse)
                                    : ("Изменен согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workLittleDto) + " по версии ТЗ " + workAgreementResponse.getRequest().getVersion() + "\n" + workAgreementResponse.compareObj(workAgreementResponseSave))
                    )
            );
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), Arrays.stream(ex.getStackTrace()).toArray());
        }
        return workAgreementResponse;
    }

    public void deleteWorkAgreementResponse(String author, Long id) {
        WorkAgreementResponse workAgreementResponseSave = findById(id).orElse(null);
        if (workAgreementResponseSave == null) {
            return;
        }
        workAgreementResponseRepository.deleteById(id);
        WorkLittleDto workLittleDto = workServiceIntegration.getWorEditDto(workAgreementResponseSave.getWorkId());

        infoServiceIntegration.addMessage(
                new MessageInfoDto(
                        author,
                        MessageType.EDIT_WORK_REQUEST,
                        ("Удалено согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workAgreementResponseSave.getWorkId(), workLittleDto.getName()) + " по версии " + workAgreementResponseSave.getRequest().getVersion() + "\n" + workAgreementResponseSave)
                )
        );

    }

    public List<WorkAgreementResponse> findWorkAgreementResponse(Long workId, Long requestId) {
        return findWorkAgreementResponse(workId, workAgreementRequestService.findRequest(requestId));

    }

    public List<WorkAgreementResponse> findWorkAgreementResponse(Long workId, WorkAgreementRequest request) {
        Specification<@NonNull WorkAgreementResponse> specification = Specification.unrestricted();
        specification = Specification.where(Specifications.eq(specification, "workId", workId));
        specification = Specifications.eq(specification, "request", request);
        return workAgreementResponseRepository.findAll(specification, Sort.by("workId").and(Sort.by("requestId").and(Sort.by("timestamp"))));
    }

}
