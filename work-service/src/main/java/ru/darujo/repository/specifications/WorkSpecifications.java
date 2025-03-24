package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;


public class WorkSpecifications {
    public static Specification<Work> workNameLike(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),String.format("%%%s%%",name).toUpperCase()));
    }

    public static Specification<Work> stageZiLe(Integer stageZiLe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("stageZI"),stageZiLe));
    }
    public static Specification<Work> stageZiGe(Integer stageZiGe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("stageZI"),stageZiGe));
    }

    public static Specification<Work> stageZiEq(Integer stageZi) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stageZI"),stageZi));
    }
    public static Specification<WorkLittle> workLittleNameLike(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),String.format("%%%s%%",name).toUpperCase()));
    }

    public static Specification<WorkLittle> workLittleStageZiLe(Integer stageZiLe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("stageZI"),stageZiLe));
    }
    public static Specification<WorkLittle> workLittleStageZiGe(Integer stageZiGe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("stageZI"),stageZiGe));
    }
    public static Specification<Work> codeSapEq(Long codeSap) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("codeSap"),codeSap));

    }

    public static Specification<Work> codeZiLike(String codeZi) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("codeZI")),String.format("%%%s%%",codeZi).toUpperCase()));
    }

    public static Specification<Work> taskLike(String task) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("task")),String.format("%%%s%%",task).toUpperCase()));
    }


    public static Specification<Work> releaseLike(String release) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("release")),String.format("%%%s%%",release).toUpperCase()));
    }
//нужно для того чтобы записи на разных страницах не повторялись
    public static Specification<Work> queryDistinctTrue() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        });
    }

    public static Specification<WorkLittle> queryDistinctTrueLittle() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        });
    }
}
