package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Work;


public class WorkSpecifications {
    public static Specification<Work> workNameLike(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),String.format("%%%s%%",name).toUpperCase()));
    }

    public static Specification<Work> stageZiLt(Integer stageZiLt) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("stageZI"),stageZiLt));
    }
}
