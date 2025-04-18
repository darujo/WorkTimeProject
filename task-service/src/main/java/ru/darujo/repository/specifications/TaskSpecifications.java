package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Task;


public class TaskSpecifications {
    public static Specification<Task> workIdEQ(Long workId){
        return eq("workId",workId);
    }
    public static Specification<Task> typeEq(Integer type) {
        return eq("type",type);
    }
    public static Specification<Task> like(String field, String value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(field)),String.format("%%%s%%",value).toUpperCase())
        );
    }
    public static Specification<Task> notEqual(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field),value)
        );
    }
    public static Specification<Task> eq(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<Task> eq(String field, Integer value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));
    }
    public static Specification<Task> queryDistinctTrue() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        });
    }

}
