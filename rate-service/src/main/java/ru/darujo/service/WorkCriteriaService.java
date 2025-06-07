package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.WorkCriteria;
import ru.darujo.repository.WorkCriteriaRepository;
import ru.darujo.repository.specifications.WorkCriteriaSpecifications;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class WorkCriteriaService {
    private WorkCriteriaRepository workCriteriaRepository;
    @Autowired
    public void setWorkCriteriaRepository(WorkCriteriaRepository workCriteriaRepository) {
        this.workCriteriaRepository = workCriteriaRepository;
    }

    public Optional<WorkCriteria> findById(long id) {
        return workCriteriaRepository.findById(id);
    }
    private void validWorkCriteria(WorkCriteria workCriteria){
        if (workCriteria.getWorkId() == null) {
            throw new ResourceNotFoundException("Не могу найти привязку к ЗИ");
        }
        if (workCriteria.getCriteria() == null && workCriteria.getCriteria() < 1)  {
            throw new ResourceNotFoundException("Не заполнено ФИО");
        }
        Specification<WorkCriteria> specification = Specification.where(WorkCriteriaSpecifications.workIdEq(workCriteria.getWorkId()));
        specification = WorkCriteriaSpecifications.eq(specification,"criteria",workCriteria.getCriteria());
        specification = WorkCriteriaSpecifications.ne(specification,"id", workCriteria.getId());
        WorkCriteria workCriteriaFind = workCriteriaRepository.findOne(specification).orElse(null);
        if(workCriteriaFind != null){
            throw new ResourceNotFoundException("Уже есть запись с таким критерием");
        }

    }
    public WorkCriteria saveWorkCriteria(WorkCriteria workCriteria) {
        validWorkCriteria(workCriteria);
        return workCriteriaRepository.save(workCriteria);
    }

    public void deleteWorkStage(Long id) {
        workCriteriaRepository.deleteById(id);
    }


    public List<WorkCriteria> findWorkCriteria(Long workId) {
        Specification<WorkCriteria> specification = Specification.where(WorkCriteriaSpecifications.workIdEq(workId));


        return workCriteriaRepository.findAll(specification, Sort.by("workId").and(Sort.by("criteria")));
    }

}
