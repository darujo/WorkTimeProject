package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Task;

import java.util.List;


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
    public static Specification<Task> like(String field, String value, Specification<Task> specification) {
        if (value != null && !value.equals("")) {
            specification = specification.and(TaskSpecifications.like(field, value));
        }
        return specification;
    }
    public static Specification<Task> in(Specification<Task> specification, String field, List<Long> value){
        if(value != null && !value.isEmpty()){
            specification = specification.and(in(field,value));
        }
        return specification;
    }
    public static Specification<Task> in(String field, List<Long> value){
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }

}
