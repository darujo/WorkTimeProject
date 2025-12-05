package ru.darujo.repository;

import lombok.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkCriteria;

@Repository
@Primary
public interface WorkCriteriaRepository extends CrudRepository<@NonNull WorkCriteria, @NonNull Long>, JpaSpecificationExecutor<@NonNull WorkCriteria> {
}
