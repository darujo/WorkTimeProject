package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<@NonNull Category, @NonNull Long>, JpaSpecificationExecutor<@NonNull Category> {
    Optional<Category> findByNameIgnoreCase(String name);
}
