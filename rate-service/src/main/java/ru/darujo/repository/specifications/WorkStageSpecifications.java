package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.WorkStage;

public class WorkStageSpecifications {
    public static Specification<WorkStage> workIdEq(Long workId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("workId"),workId));
    }
    public static Specification<WorkStage> roleEq(Integer role){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"),role));
    }
    public static Specification<WorkStage> nikNameEq(String nikName){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nikName"),nikName));
    }

}
