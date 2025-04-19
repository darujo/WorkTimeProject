package ru.darujo.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.Role;

public class RoleSpecifications {
    public static Specification<Role> like(Specification<Role> specification, String field, String value) {
        if (value != null && !value.isEmpty()) {
            specification = specification.and(like(field, value));
        }
        return specification;
    }

    public static Specification<Role> like(String field, String value) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                        String.format("%%%s%%", value).toLowerCase()));
    }
}
