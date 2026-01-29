package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Right;

import java.util.Optional;

@Repository
public interface RightRepository extends CrudRepository<@NonNull Right, @NonNull Long> {
    Optional<Right> findByNameIgnoreCase(String name);
}
