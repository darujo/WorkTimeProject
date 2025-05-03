package ru.darujo.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Work;
import ru.darujo.model.WorkLittle;


public class WorkSpecifications {

    public static Specification<Work> stageZiLe(Integer stageZiLe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("stageZI"), stageZiLe));
    }

    public static Specification<Work> stageZiGe(Integer stageZiGe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("stageZI"), stageZiGe));
    }

    public static Specification<Work> stageZiEq(Integer stageZi) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("stageZI"), stageZi));
    }

    public static Specification<WorkLittle> workLittleStageZiLe(Integer stageZiLe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("stageZI"), stageZiLe));
    }

    public static Specification<WorkLittle> workLittleStageZiGe(Integer stageZiGe) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("stageZI"), stageZiGe));
    }

    public static Specification<Work> eq(Specification<Work> specification, String field, Long value) {
        if (value != null) {
            specification = specification.and(eq(field, value));
        }
        return specification;
    }

    private static Specification<Work> eq(String field, Long value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

    //нужно для того чтобы записи на разных страницах не повторялись
    public static Specification<Work> queryDistinctTrue() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(false);
            return null;
        });
    }

    public static Specification<WorkLittle> queryDistinctTrueLittle() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        });
    }

    public static Specification<Work> like(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), String.format("%%%s%%", value).toUpperCase()));
    }

    public static Specification<WorkLittle> likeLittle(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), String.format("%%%s%%", value).toUpperCase()));
    }

    public static Specification<WorkLittle> eqLittle(Specification<WorkLittle> specification, String field, Long value) {
        if (value != null) {
            specification = specification.and(eqLittle(field, value));
        }
        return specification;
    }

    private static Specification<WorkLittle> eqLittle(String field, Long value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

}
