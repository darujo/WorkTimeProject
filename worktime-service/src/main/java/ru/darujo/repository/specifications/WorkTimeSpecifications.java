package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkTime;

import java.util.Date;

public class WorkTimeSpecifications {
    public static Specification<WorkTime> dateGE(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workDate"),date));
    }
    public static Specification<WorkTime> dateLE(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workDate"),date));
    }
    public static Specification<WorkTime> dateGT(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("workDate"),date));
    }
    public static Specification<WorkTime> workIDEQ(Long work_id){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("workId"),work_id));
    }
    public static Specification<WorkTime> userNameEQ(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userName"),name));
    }

//    public static Specification<WorkTime> titleLike(String title){
//        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),String.format("%%%s%%",title)));
//    }
}
