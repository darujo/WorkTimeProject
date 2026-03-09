package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Release;
import ru.darujo.model.ReleaseProject;

@Repository
@Primary
public interface ReleaseProjectRepository extends CrudRepository<@NonNull ReleaseProject, @NonNull Long>, JpaSpecificationExecutor<@NonNull ReleaseProject> {
    ReleaseProject findByReleaseAndProjectId(Release release, Long projectId);
}
