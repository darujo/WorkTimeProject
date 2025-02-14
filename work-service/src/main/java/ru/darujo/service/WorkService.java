package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.darujo.model.Work;
import ru.darujo.repository.WorkRepository;
import ru.darujo.repository.specifications.WorkSpecifications;

import java.util.Optional;

@Service
public class WorkService {
    private WorkRepository workRepository;
    private int size = 10;
    private Page<Work> workPage;
    @Autowired
    public void setWorkRepository(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

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
        if (workPage != null && page == 1 && this.size ==size){
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

}
