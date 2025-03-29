package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkCriteria;

public class WorkCriteriaSpecifications {
    public static Specification<WorkCriteria> workIdEq(Long workId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("workId"),workId));
    }
    public static Specification<WorkCriteria> criteriaEq(Integer criteria){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("criteria"),criteria));
    }
}
