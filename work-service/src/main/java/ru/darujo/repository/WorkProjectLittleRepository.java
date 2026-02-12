package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkLittle;
import ru.darujo.model.WorkProjectLittle;

@Repository
@Primary
public interface WorkProjectLittleRepository extends CrudRepository<@NonNull WorkProjectLittle, @NonNull Long>, JpaSpecificationExecutor<@NonNull WorkProjectLittle> {
    WorkProjectLittle findByWorkLittleAndProjectId(WorkLittle work, Long projectId);
}

