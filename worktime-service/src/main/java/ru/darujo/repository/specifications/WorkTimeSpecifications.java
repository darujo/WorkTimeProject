package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkTime;

import java.util.Date;

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
    public static Specification<WorkTime> userNikNameEQ(String nikName){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nikName"),nikName));
    }

    public static Specification<WorkTime> typeEq(Integer type) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"),type));
    }

    public static Specification<WorkTime> like(String field, String value){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                                     String.format("%%%s%%",value).toLowerCase()));
    }
}
