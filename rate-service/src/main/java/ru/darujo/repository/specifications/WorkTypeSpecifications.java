package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkType;

public class WorkTypeSpecifications {
    public static Specification<WorkType> workIdEq(Long workId){
        return eq("workId",workId);
    }

    public static Specification<WorkType> eq(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }

    public static Specification<WorkType> eq(Specification<WorkType> specification, String field, Integer value){
        if (value != null){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    public static Specification<WorkType> eq(String field, Integer value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<WorkType> ne(Specification<WorkType> specification, String field, Long value){
        if (value != null){
            specification = specification.and(ne(field,value));
        }
        return specification;
    }
    public static Specification<WorkType> ne(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field),value));
    }

    public static Specification<WorkType> eqIgnoreCase(Specification<WorkType> specification, String field, String value){
        if (value != null){
            specification = specification.and(eqIgnoreCase(field,value));
        }
        return specification;
    }
    private static Specification<WorkType> eqIgnoreCase(String field, String value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get(field)),value.toLowerCase()));
    }
}
