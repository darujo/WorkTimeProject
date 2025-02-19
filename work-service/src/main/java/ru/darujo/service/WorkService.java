package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.dto.WorkRepDto;
import ru.darujo.integration.WorkTimeServiceIntegration;
import ru.darujo.model.Work;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkService {
    private WorkTimeServiceIntegration workTimeServiceIntegration ;
    @Autowired
    public void setWorkTimeServiceIntegration(WorkTimeServiceIntegration workTimeServiceIntegration) {
        this.workTimeServiceIntegration = workTimeServiceIntegration;
    }
    private WorkRepository workRepository;
    @Autowired
    public void setWorkRepository(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }
    private int size = 10;
    private Page<Work> workPage;

    public Optional<Work> findById(long id) {
         return workRepository.findById(id);
    }

    public Work saveWork(Work work) {
        size = -1;
        return workRepository.save(work);
    }

    public void deleteWork(Long id) {
        workRepository.deleteById(id);
        size = -1;
    }
    public Page<Work> findWorks(int page, int size,String name) {
        if (workPage != null && page == 1 && this.size ==size && name == null){
            return workPage;
        }
        Specification<Work> specification = Specification.where(null);

//        if (dateLE != null) {
//            specification = specification.and(WorkTimeSpecifications.dateLE(dateLE));
//        }
//        if (dateGE != null) {
//            specification = specification.and(WorkTimeSpecifications.dateGE(dateGE));
//        }
        if (name != null) {
            specification = specification.and(WorkSpecifications.workNameLike(name));
        }
        workPage = workRepository.findAll(specification, PageRequest.of(page - 1, size));
        this.size = size;
        return workPage;
    }
    public List<WorkRepDto> getWorkRep(String userName){
        List<WorkRepDto> workRepDtos = new ArrayList<>();
        workRepository.findAll().forEach(work ->
            workRepDtos.add(new WorkRepDto(
                work.getName(),
                workTimeServiceIntegration.getTimeWork(
                        work.getId(),
                        userName,
                        null,
                        work.getDateStartDevelop()),
                workTimeServiceIntegration.getTimeWork(work.getId(),
                        userName,
                        work.getDateStartDevelop(),
                        work.getDateStartDebug()),
                workTimeServiceIntegration.getTimeWork(work.getId(),
                        userName,
                        work.getDateStartDebug(),
                        work.getDateStartRelease()),
                workTimeServiceIntegration.getTimeWork(work.getId(),
                        userName,
                        work.getDateStartRelease(),
                        work.getDateStartOPE()),
                workTimeServiceIntegration.getTimeWork(work.getId(),
                        userName,
                        work.getDateStartOPE(),
                        work.getDateStartWender()),
                workTimeServiceIntegration.getTimeWork(work.getId(),
                        userName,
                        work.getDateStartWender(),
                        null))
               )
           );
        return workRepDtos;
    }

}
