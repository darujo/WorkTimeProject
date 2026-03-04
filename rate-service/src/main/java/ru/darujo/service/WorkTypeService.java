package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkType;
import ru.darujo.repository.WorkTypeRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkTypeService {
    private WorkTypeRepository workTypeRepository;
    private WorkServiceIntegration workServiceIntegration;

    @Autowired
    public void setWorkCriteriaRepository(WorkTypeRepository workTypeRepository) {
        this.workTypeRepository = workTypeRepository;
    }

    public Optional<WorkType> findById(long id) {
        return workTypeRepository.findById(id);
    }

    private void validWorkType(WorkType workType) {
        if (workType.getWorkId() == null) {
            throw new ResourceNotFoundRunTime("Не могу найти привязку к ЗИ");
        }
        Specification<@NonNull WorkType> specification = Specification.where(Specifications.eq(null, "workId", workType.getWorkId()));
        specification = Specifications.eqIgnoreCase(specification, "type", workType.getType());
        specification = Specifications.ne(specification, "id", workType.getId());
        specification = Specifications.eq(specification, "projectId", workType.getProjectId());

        WorkType workTypeFind = workTypeRepository.findOne(specification).orElse(null);
        if (workTypeFind != null) {
            throw new ResourceNotFoundRunTime("Уже есть запись с такой работой");
        }

    }

    public WorkType saveWorkType(WorkType workType) {
        validWorkType(workType);
        workServiceIntegration.addProject(workType.getWorkId(), workType.getProjectId());
        return workTypeRepository.save(workType);
    }

    public void deleteWorkType(Long id) {
        workTypeRepository.deleteById(id);
    }


    public List<WorkType> findWorkCriteria(Long workId, Long projectId) {
        Specification<@NonNull WorkType> specification = Specification.where(Specifications.eq(null, "workId", workId));
        specification = Specifications.eq(specification, "projectId", projectId);
        return workTypeRepository.findAll(specification, Sort.by("workId").and(Sort.by("number").and(Sort.by("type"))));
    }

    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }
}
