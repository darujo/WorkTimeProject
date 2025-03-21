package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Task;


public class TaskSpecifications {
    public static Specification<Task> codeBTSLike(String codeBTS){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("codeBTS"),String.format("%%%s%%",codeBTS)));
    }
    public static Specification<Task> codeDEVBOLike(String codeDEVBO){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("codeDEVBO"),String.format("%%%s%%",codeDEVBO)));
    }
    public static Specification<Task> descriptionLike(String description){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"),String.format("%%%s%%",description)));
    }
    public static Specification<Task> workIdEQ(Long workId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("workId"),workId));
    }
    public static Specification<Task> nikNameLike(String nikName){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nikName"),String.format("%%%s%%",nikName)));
    }

    public static Specification<Task> typeEq(Integer type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"),type);
    }
}
