package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkTime;

import java.util.Date;

public class WorkTimeSpecifications {
    public static Specification<WorkTime> dateGE(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workTime"),date));
    }
    public static Specification<WorkTime> dateLE(Date date){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workTime"),date));
    }
    public static Specification<WorkTime> workIDEQ(Long id){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("work_id"),id));
    }
//    public static Specification<WorkTime> titleLike(String title){
//        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),String.format("%%%s%%",title)));
//    }
}
