package ru.darujo.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkStage;

@Repository
@Primary
public interface WorkStageRepository extends CrudRepository<WorkStage,Long>, JpaSpecificationExecutor<WorkStage> {
}
