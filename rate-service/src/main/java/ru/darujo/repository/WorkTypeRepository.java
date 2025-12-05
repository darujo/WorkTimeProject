package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkType;

@Repository
@Primary
public interface WorkTypeRepository extends CrudRepository<@NonNull WorkType, @NonNull Long>, JpaSpecificationExecutor<@NonNull WorkType> {
}
