package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkCriteria;

public class WorkCriteriaSpecifications {
    public static Specification<WorkCriteria> workIdEq(Long workId){
        return eq("workId",workId);
    }

    public static Specification<WorkCriteria> eq(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }

    public static Specification<WorkCriteria> eq(Specification<WorkCriteria> specification, String field, Integer value){
        if (value != null){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    public static Specification<WorkCriteria> eq(String field, Integer value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<WorkCriteria> ne(Specification<WorkCriteria> specification, String field, Long value){
        if (value != null){
            specification = specification.and(ne(field,value));
        }
        return specification;
    }
    public static Specification<WorkCriteria> ne(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field),value));
    }
}
