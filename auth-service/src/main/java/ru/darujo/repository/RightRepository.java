package ru.darujo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Right;

import java.util.Optional;

@Repository
public interface RightRepository extends CrudRepository<Right,Long> {
    Optional<Right> findByNameIgnoreCase(String right);

    Optional<Object> findByNameIgnoreCaseAndIdIsNot(String name, long id);
}
