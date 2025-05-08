package ru.darujo.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Vacation;

import java.util.Date;
import java.util.List;

public class VacationSpecifications {
    public static Specification<Vacation> dateGe(Specification<Vacation> specification,String field, Date value){
        if(value != null){
            specification = specification.and(dateGe(field,value));
        }
        return specification;
    }
    private static Specification<Vacation> dateGe(String field, Date date) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), date));
    }
    public static Specification<Vacation> dateLe(Specification<Vacation> specification,String field, Date value){
        if(value != null){
            specification = specification.and(dateLe(field,value));
        }
        return specification;
    }
    private static Specification<Vacation> dateLe(String field, Date date) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field), date));
    }

    public static Specification<Vacation> in(Specification<Vacation> specification, String field, List<String> value) {
        if (value != null && value.size() > 0) {
            if (value.size() == 1) {
                specification = eq(specification, field, value.get(0));
            } else {
                specification = specification.and(in(field, value));
            }
        }
        return specification;
    }

    private static Specification<Vacation> in(String field, List<String> value) {
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }

    public static Specification<Vacation> eq(Specification<Vacation> specification, String field, String value) {
        if (value != null) {
            specification = specification.and(eq(field, value));
        }
        return specification;
    }

    private static Specification<Vacation> eq(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

}
