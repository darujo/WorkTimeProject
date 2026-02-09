package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkCriteria;
import ru.darujo.repository.WorkCriteriaRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkCriteriaService {
    private WorkCriteriaRepository workCriteriaRepository;
    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkCriteriaRepository(WorkCriteriaRepository workCriteriaRepository) {
        this.workCriteriaRepository = workCriteriaRepository;
    }

    public Optional<WorkCriteria> findById(long id) {
        return workCriteriaRepository.findById(id);
    }

    private void validWorkCriteria(WorkCriteria workCriteria) {
        if (workCriteria.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        if (workCriteria.getCriteria() == null || workCriteria.getCriteria() < 1) {
            throw new ResourceNotFoundRunTime("Не заполнено критерий");
        }
        Specification<@NonNull WorkCriteria> specification = Specification.where(Specifications.eq(null, "workId", workCriteria.getWorkId()));
        specification = Specifications.eq(specification, "criteria", workCriteria.getCriteria());
        specification = Specifications.ne(specification, "id", workCriteria.getId());
        WorkCriteria workCriteriaFind = workCriteriaRepository.findOne(specification).orElse(null);
        if (workCriteriaFind != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с таким критерием");
        }

    }

    public WorkCriteria saveWorkCriteria(WorkCriteria workCriteria) {
        validWorkCriteria(workCriteria);
        workServiceIntegration.addProject(workCriteria.getWorkId(), workCriteria.getProjectId());
        return workCriteriaRepository.save(workCriteria);
    }

    public void deleteWorkCriteria(Long id) {
        workCriteriaRepository.deleteById(id);
    }


    public List<WorkCriteria> findWorkCriteria(Long workId, Long projectId) {
        Specification<@NonNull WorkCriteria> specification = Specification.where(Specifications.eq(null, "workId", workId));
        specification = Specifications.eq(specification, "projectId", projectId);
        return workCriteriaRepository.findAll(specification, Sort.by("workId").and(Sort.by("criteria")));
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }
}
