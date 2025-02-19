package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.WorkDto;
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
        WorkDto workDto = workServiceIntegration.getWork(workTime.getWorkId());
        return workTimeRepository.save(workTime);
    }

    public void deleteWorkTime(Long id) {
        workTimeRepository.deleteById(id);
        workTimePage = null;
    }

    public Page<WorkTime> findWorkTime(Long workID, String userName, Date dateLE, Date dateGT, Date dateGE, int page, int size) {
        Specification<WorkTime> specification = Specification.where(null);
        if (dateLE != null) {
            specification = specification.and(WorkTimeSpecifications.dateLE(dateLE));
        }
        if (dateGE != null) {
            specification = specification.and(WorkTimeSpecifications.dateGE(dateGE));
        }
        if (dateGT != null) {
            specification = specification.and(WorkTimeSpecifications.dateGT(dateGT));
        }
        if (userName != null) {
            specification = specification.and(WorkTimeSpecifications.userNameEQ(userName));
        }
        if (workID != null) {
            specification = specification.and(WorkTimeSpecifications.workIDEQ(workID));
        }
        workTimePage = workTimeRepository.findAll(specification, PageRequest.of(page - 1, size));
        System.out.println(workTimePage);
        this.size = size;
        return workTimePage;
    }
    public float getTimeWork(Long workID, String username,Date dateLE, Date dateGT){
        return findWorkTime(workID, username, dateLE, dateGT,null,1, 100000).getContent().stream().map(WorkTime::getWorkTime).reduce((sumTime, time)-> sumTime + time).orElse(0f) ;
    }
}
