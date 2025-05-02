package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkTime;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WorkTimeSpecifications {
    public static Specification<WorkTime> dateGE(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workDate"),date));
    }
    public static Specification<WorkTime> dateLe(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workDate"),date));
    }
    public static Specification<WorkTime> dateLt(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("workDate"),date));
    }
    public static Specification<WorkTime> dateGT(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("workDate"),date));
    }
    public static Specification<WorkTime> taskIdEQ(Long taskId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taskId"),taskId));
    }
    public static Specification<WorkTime> typeEq(Integer type) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"),type));
    }

    public static Specification<WorkTime> like(String field, String value){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                                     String.format("%%%s%%",value).toLowerCase()));
    }

    public static Specification<WorkTime> in(Specification<WorkTime> specification, String field, Long[] value){
        if(value != null && value.length > 0){
            if(value.length == 1){
                specification = eq (specification,field,value[0]);
            } else {
                specification = specification.and(in(field, Arrays.asList(value)));
            }
        }
        return specification;
    }
    private static Specification<WorkTime> in(String field, List<Long> value){
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }
    public static Specification<WorkTime> eq(Specification<WorkTime> specification, String field, Long value){
        if(value != null){
            specification = specification.and(eq(field,value));
        }
        return specification;
    }
    private static Specification<WorkTime> eq(String field, Long value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));

    }
    public static Specification<WorkTime> in(Specification<WorkTime> specification, String field, List<String> value){
        if(value != null && value.size() > 0){
            if(value.size() == 1){
                specification = specification.and(eq (field,value.get(0)));
            } else {
                specification = specification.and(inString(field, value));
            }
        }
        return specification;
    }
    private static Specification<WorkTime> inString(String field, List<String> value){
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }
    private static Specification<WorkTime> eq(String field, String value){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field),value));

    }
}
