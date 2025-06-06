package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkStage;

public class WorkStageSpecifications {
    public static Specification<WorkStage> workIdEq(Long workId){
        return eq("workId",workId);
    }

    public static Specification<WorkStage> eq(Specification<WorkStage> specification, String field, Long value){
        if (value != null){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    public static Specification<WorkStage> eq(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<WorkStage> eq(Specification<WorkStage> specification, String field, String value){
        if (value != null && !value.equals("")){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    public static Specification<WorkStage> eq(String field, String value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }

    public static Specification<WorkStage> eq(Specification<WorkStage> specification, String field, Integer value){
        if (value != null){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    public static Specification<WorkStage> eq(String field, Integer value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<WorkStage> ne(Specification<WorkStage> specification, String field, Long value){
        if (value != null){
            specification = specification.and(ne(field,value));
        }
        return specification;
    }
    public static Specification<WorkStage> ne(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field),value));
    }
}
