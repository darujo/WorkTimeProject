package ru.darujo.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.WorkAgreementRequest;
import ru.darujo.repository.WorkAgreementRequestRepository;
import ru.darujo.specifications.Specifications;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkAgreementRequestService {
    private WorkAgreementRequestRepository workAgreementRequestRepository;

    @Autowired
    public void setWorkAgreementRequestRepository(WorkAgreementRequestRepository workAgreementRequestRepository) {
        this.workAgreementRequestRepository = workAgreementRequestRepository;
    }
    @Getter
    private static WorkAgreementRequestService instance;

    @PostConstruct
    public void init(){
        instance = this;
    }

    public Optional<WorkAgreementRequest> findById(long id) {
        return workAgreementRequestRepository.findById(id);
    }
    public WorkAgreementRequest findRequest(Long id) {
        if (id == null){
            return null;
        }
        return findById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Не найден запрос с ID = " + id));
    }

    private void validWorkAgreementRequest(WorkAgreementRequest workAgreementRequest) {
        if (workAgreementRequest.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        if (workAgreementRequest.getVersion() == null || workAgreementRequest.getVersion().isEmpty()) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        Specification<WorkAgreementRequest> specification = Specification.where(Specifications.eq(null, "workId", workAgreementRequest.getWorkId()));
        specification = Specifications.eq(specification, "version", workAgreementRequest.getVersion());
        specification = Specifications.ne(specification, "id", workAgreementRequest.getId());
        WorkAgreementRequest workCriteriaFind = workAgreementRequestRepository.findOne(specification).orElse(null);
        if (workCriteriaFind != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с таким критерием");
        }

    }

    public WorkAgreementRequest saveWorkCriteria(WorkAgreementRequest workAgreementRequest) {
        validWorkAgreementRequest(workAgreementRequest);
        return workAgreementRequestRepository.save(workAgreementRequest);
    }

    public void deleteWorkCriteria(Long id) {
        workAgreementRequestRepository.deleteById(id);
    }


    public List<WorkAgreementRequest> findWorkAgreementRequest(Long workId) {
        Specification<WorkAgreementRequest> specification = Specification.where(Specifications.eq(null, "workId", workId));
        return workAgreementRequestRepository.findAll(specification, Sort.by("workId").and(Sort.by("version")));
    }

}
