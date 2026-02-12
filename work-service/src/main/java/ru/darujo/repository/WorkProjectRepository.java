package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Work;
import ru.darujo.model.WorkProject;

@Repository
@Primary
public interface WorkProjectRepository extends CrudRepository<@NonNull WorkProject, @NonNull Long>, JpaSpecificationExecutor<@NonNull WorkProject> {
    WorkProject findByWorkAndProjectId(Work work, Long projectId);
}
