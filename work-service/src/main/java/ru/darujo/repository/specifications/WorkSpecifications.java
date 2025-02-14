package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Work;


public class WorkSpecifications {
//    public static Specification<WorkTime> dateGE(Date date){
//        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("workTime"),date));
//    }
//    public static Specification<WorkTime> dateLE(Date date){
//        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("workTime"),date));
//    }
//    public static Specification<WorkTime> workIDEQ(Long id){
//        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("work_id"),id));
//    }
    public static Specification<Work> workNameLike(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),String.format("%%%s%%",name)));
    }
}
