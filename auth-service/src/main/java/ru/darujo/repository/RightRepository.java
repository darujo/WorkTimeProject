package ru.darujo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Right;

@Repository
public interface RightRepository extends CrudRepository<Right,Long> {
}
