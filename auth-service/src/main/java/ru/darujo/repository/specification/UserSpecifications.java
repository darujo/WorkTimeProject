package ru.darujo.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.darujo.model.User;

import java.util.List;

public class UserSpecifications {
    public static Specification<User> like(Specification<User> specification, String field, String value){
        if(value != null && !value.isEmpty()){
            specification = specification.and(like(field,value));
        }
        return specification;
    }
    public static Specification<User> like(String field, String value){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)),
                                     String.format("%%%s%%",value).toLowerCase()));
    }
    public static Specification<User> in(Specification<User> specification, String field, List<String> value){
        if(value != null && !value.isEmpty()){
            specification = specification.and(in(field,value));
        }
        return specification;
    }
    public static Specification<User> in(String field, List<String> value){
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }
}
