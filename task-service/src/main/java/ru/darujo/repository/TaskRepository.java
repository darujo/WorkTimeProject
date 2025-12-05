package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<@NonNull Task, @NonNull Long>, JpaSpecificationExecutor<@NonNull Task> {
    List<Task> findFirst10ByCodeBTSLikeIgnoreCase(String codeBTS );
    List<Task> findFirst10ByCodeDEVBOLikeIgnoreCase(String codeBTS );
}