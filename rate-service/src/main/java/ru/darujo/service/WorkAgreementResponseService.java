package ru.darujo.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.WorkAgreementResponse;
import ru.darujo.repository.WorkAgreementResponseRepository;
import ru.darujo.specifications.Specifications;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkAgreementResponseService {
    private WorkAgreementResponseRepository workAgreementResponseRepository;

    @Autowired
    public void setWorkAgreementResponseRepository(WorkAgreementResponseRepository workAgreementResponseRepository) {
        this.workAgreementResponseRepository = workAgreementResponseRepository;
    }
    @Getter
    private static WorkAgreementResponseService INSTANCE;
    @PostConstruct
    public void init(){
        INSTANCE = this;
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
        Specification<WorkAgreementResponse> specification = Specification.where(Specifications.eq(null, "workId", workAgreementResponse.getWorkId()));
        specification = Specifications.eq(specification, "request", workAgreementResponse.getRequest().getId());
        specification = Specifications.eq(specification, "niKName", workAgreementResponse.getNikName());
        specification = Specifications.ne(specification, "id", workAgreementResponse.getId());
        WorkAgreementResponse workAgreementResponseSave = workAgreementResponseRepository.findOne(specification).orElse(null);
        if (workAgreementResponseSave != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с вашим согласованием");
        }

    }

    public WorkAgreementResponse saveWorkAgreementResponse(WorkAgreementResponse workAgreementResponse) {
        validWorkAgreementResponse(workAgreementResponse);
        return workAgreementResponseRepository.save(workAgreementResponse);
    }

    public void deleteWorkAgreementResponse(Long id) {
        workAgreementResponseRepository.deleteById(id);
    }


    public List<WorkAgreementResponse> findWorkAgreementResponse(Long workId,Long requestId) {
        Specification<WorkAgreementResponse> specification = Specification.where(Specifications.eq(null, "workId", workId));
        specification = Specifications.eq(specification, "request", requestId);
        return workAgreementResponseRepository.findAll(specification, Sort.by("workId").and(Sort.by("requestId").and(Sort.by("timestamp"))));
    }

}
