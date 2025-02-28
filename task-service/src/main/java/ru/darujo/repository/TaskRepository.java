package ru.darujo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.Task;

public interface TaskRepository extends CrudRepository<Task,Long>, JpaSpecificationExecutor<Task> {
}