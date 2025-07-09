package ru.darujo.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Specifications {

    public static <T> Specification<T> ge(Specification<T> specification, String field, Integer value) {
        if (value != null) {
            specification = specification.and(ge(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> ge(String field, Integer value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), value));

    }

    public static <T> Specification<T> le(Specification<T> specification, String field, Integer value) {
        if (value != null) {
            specification = specification.and(le(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> le(String field, Integer value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field), value));
    }

    public static <T> Specification<T> ge(Specification<T> specification, String field, Date value) {
        if (value != null) {
            specification = specification.and(ge(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> ge(String field, Date value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), value));

    }

    public static <T> Specification<T> le(Specification<T> specification, String field, Date value) {
        if (value != null) {
            specification = specification.and(le(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> le(String field, Date value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field), value));
    }

    public static <T> Specification<T> gt(Specification<T> specification, String field, Date value) {
        if (value != null) {
            specification = specification.and(gt(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> gt(String field, Date value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(field), value));

    }

    public static <T> Specification<T> lt(Specification<T> specification, String field, Date value) {
        if (value != null) {
            specification = specification.and(lt(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> lt(String field, Date value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(field), value));
    }

    public static <T> Specification<T> eq(Specification<T> specification, String field, Integer value) {
        if (value != null) {
            specification = specification.and(eq(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> eq(String field, Integer value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

    public static <T> Specification<T> eq(Specification<T> specification, String field, Long value) {
        if (value != null) {
            if (specification == null) {
                specification = eq(field, value);
            } else {
                specification = specification.and(eq(field, value));
            }
        }
        return specification;
    }

    private static <T> Specification<T> eq(String field, Long value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

    //нужно для того чтобы записи на разных страницах не повторялись
    public static <T> Specification<T> queryDistinctTrue() {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(false);
            return null;
        });
    }

    public static <T> Specification<T> like(Specification<T> specification, String field, String value) {
        if (value != null) {
            specification = specification.and(like(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> like(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), String.format("%%%s%%", value).toUpperCase()));
    }

    public static <T> Specification<T> in(Specification<T> specification, String field, Long[] value) {
        if (value != null && value.length > 0) {
            if (value.length == 1) {
                specification = eq(specification, field, value[0]);
            } else {
                specification = specification.and(in(field, Arrays.asList(value)));
            }
        }
        return specification;
    }

    public static <T> Specification<T> inLong(Specification<T> specification, String field, List<Long> value) {
        if (value != null && value.size() > 0) {
            if (value.size() == 1) {
                specification = eq(specification, field, value.get(0));
            } else {
                specification = specification.and(in(field, value));
            }
        }
        return specification;
    }

    private static <T> Specification<T> in(String field, List<Long> value) {
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }

    public static <T> Specification<T> in(Specification<T> specification, String field, List<String> value) {
        if (value != null && value.size() > 0) {
            if (value.size() == 1) {
                specification = specification.and(eq(field, value.get(0)));
            } else {
                specification = specification.and(inString(field, value));
            }
        }
        return specification;
    }

    private static <T> Specification<T> inString(String field, List<String> value) {
        return ((root, query, criteriaBuilder) ->
                root.get(field).in(value));
    }

    public static <T> Specification<T> eq(Specification<T> specification, String field, String value) {
        if (value != null && !value.equals("")) {
            specification = specification.and(eq(field, value));
        }
        return specification;
    }

    private static <T> Specification<T> eq(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));

    }

    public static <T> Specification<T> notEqual(Specification<T> specification, String field, Long value) {
        if (value != null) {
            if (specification == null) {
                specification = notEqual(field, value);
            } else {
                specification = specification.and(notEqual(field, value));
            }
        }
        return specification;
    }

    private static <T> Specification<T> notEqual(String field, Long value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field), value)
        );
    }

    public static <T> Specification<T> ne(Specification<T> specification, String field, Long value) {
        if (value != null) {
            if (specification == null) {
                specification = ne(field, value);
            } else {
                specification = specification.and(ne(field, value));
            }
        }
        return specification;
    }

    public static <T> Specification<T> ne(String field, Long value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field), value));
    }

    public static <T> Specification<T> eqIgnoreCase(Specification<T> specification, String field, String value) {
        if (value != null) {
            if (specification == null) {
                specification = eqIgnoreCase(field, value);
            } else {
                specification = specification.and(eqIgnoreCase(field, value));
            }
        }
        return specification;
    }

    private static <T> Specification<T> eqIgnoreCase(String field, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get(field)), value.toLowerCase()));
    }
}
