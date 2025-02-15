package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.integration.WorkServiceIntegration;
import ru.darujo.model.WorkTime;
import ru.darujo.repository.WorkTimeRepository;
import ru.darujo.repository.specifications.WorkTimeSpecifications;

import java.util.*;

@Service
@Primary
public class WorkTimeService {
    private WorkServiceIntegration workServiceIntegration;
    @Autowired
    public void setWorkServiceIntegration(WorkServiceIntegration workServiceIntegration) {
        this.workServiceIntegration = workServiceIntegration;
    }

    private WorkTimeRepository workTimeRepository;
    private int size = 10;
    private  Page<WorkTime> workTimePage;
    @Autowired
    public void setWorkTimeRepository(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public Optional<WorkTime> findById(long id) {
        return workTimeRepository.findById(id);
    }

    public WorkTime saveWorkTime(WorkTime workTime) {
        workTimePage = null;
        workServiceIntegration.getWork(workTime.getWork());
        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
        workTimePage = null;
    }


    public Page<WorkTime> findProducts(Long workID,Date dateLE, Date dateGE, int page, int size) {
        if (workTimePage != null && page == 1 && this.size ==size && dateGE==null && dateLE== null && workID == null){
            return workTimePage;
        }
        Specification<WorkTime> specification = Specification.where(null);

        if (dateLE != null) {
            specification = specification.and(WorkTimeSpecifications.dateLE(dateLE));
        }
        if (dateGE != null) {
            specification = specification.and(WorkTimeSpecifications.dateGE(dateGE));
        }
        if (workID != null) {
            specification = specification.and(WorkTimeSpecifications.workIDEQ(workID));
        }
        workTimePage = workTimeRepository.findAll(specification, PageRequest.of(page - 1, size));
        this.size = size;
        return workTimePage;
    }
}
