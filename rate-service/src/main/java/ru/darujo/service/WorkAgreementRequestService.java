package ru.darujo.service;

import lombok.Getter;
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
import ru.darujo.repository.WorkAgreementRequestRepository;
import ru.darujo.specifications.Specifications;
import ru.darujo.url.UrlWorkTime;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkAgreementRequestService {
    private InfoServiceIntegration infoServiceIntegration;

    @Autowired
    public void setInfoServiceIntegration(InfoServiceIntegration infoServiceIntegration) {
        this.infoServiceIntegration = infoServiceIntegration;
    }


    private WorkAgreementRequestRepository workAgreementRequestRepository;

    @Autowired
    public void setWorkAgreementRequestRepository(WorkAgreementRequestRepository workAgreementRequestRepository) {
        this.workAgreementRequestRepository = workAgreementRequestRepository;
    }

    @Getter
    private static WorkAgreementRequestService instance;
    WorkAgreementResponseService workAgreementResponseService;

    @Autowired
    public void setWorkAgreementResponseService(WorkAgreementResponseService workAgreementResponseService) {
        this.workAgreementResponseService = workAgreementResponseService;
    }

    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    @PostConstruct
    public void init() {
        instance = this;
    }

    public Optional<WorkAgreementRequest> findById(long id) {
        return workAgreementRequestRepository.findById(id);
    }

    public WorkAgreementRequest findRequest(Long id) {
        if (id == null) {
            return null;
        }
        return findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден запрос с ID = " + id));
    }

    private void validWorkAgreementRequest(WorkAgreementRequest workAgreementRequest) {
        if (workAgreementRequest.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        if (workAgreementRequest.getVersion() == null || workAgreementRequest.getVersion().isEmpty()) {
            throw new ResourceNotFoundRunTime("Не заполнена версия");
        }
        Specification<WorkAgreementRequest> specification = Specification.where(Specifications.eq(null, "workId", workAgreementRequest.getWorkId()));
        specification = Specifications.eq(specification, "version", workAgreementRequest.getVersion());
        specification = Specifications.ne(specification, "id", workAgreementRequest.getId());
        WorkAgreementRequest agreementRequest = workAgreementRequestRepository.findOne(specification).orElse(null);
        if (agreementRequest != null) {
            throw new ResourceNotFoundRunTime("Уже есть запрос согласования на эту версию ТЗ");
        }

    }

    @Transactional
    public WorkAgreementRequest saveWorkCriteria(String username, WorkAgreementRequest workAgreementRequest) {
        validWorkAgreementRequest(workAgreementRequest);
        WorkAgreementRequest workAgreementRequestSave = findRequest(workAgreementRequest.getId());
        WorkLittleDto workLittleDto = workServiceIntegration.getWorEditDto(workAgreementRequest.getWorkId());

        String message = workAgreementRequestSave == null
                ?
                ("Добавлен запрос на согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workLittleDto) + "\n" + workAgreementRequest)
                : ("Изменен запрос на согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workLittleDto) + "\n" + workAgreementRequest.compareObj(workAgreementRequestSave));
        workAgreementRequest = workAgreementRequestRepository.save(workAgreementRequest);

        infoServiceIntegration.addMessage(
                new MessageInfoDto(
                        username,
                        MessageType.EDIT_WORK_REQUEST,
                        message
                )
        );

        return workAgreementRequest;
    }

    public void deleteWorkRequest(String author, Long id) {
        if (!workAgreementResponseService.findWorkAgreementResponse(null, id).isEmpty()) {
            throw new ResourceNotFoundRunTime("Нельзя удалить запрос у него уже есть согласование");
        }
        WorkAgreementRequest workAgreementRequestSave = findRequest(id);
        if (workAgreementRequestSave == null) {
            return;
        }
        WorkLittleDto workLittleDto = workServiceIntegration.getWorEditDto(workAgreementRequestSave.getWorkId());
        String message = ("Удален запрос на согласование по ЗИ " + UrlWorkTime.getUrlAgreement(workLittleDto) + "\n" + workAgreementRequestSave);
        workAgreementRequestRepository.deleteById(id);
        infoServiceIntegration.addMessage(
                new MessageInfoDto(
                        author,
                        MessageType.EDIT_WORK_REQUEST,
                        message

                )
        );
    }


    public List<WorkAgreementRequest> findWorkAgreementRequest(Long workId) {
        Specification<WorkAgreementRequest> specification = Specification.where(Specifications.eq(null, "workId", workId));
        return workAgreementRequestRepository.findAll(specification, Sort.by("workId").and(Sort.by("version")));
    }

}
