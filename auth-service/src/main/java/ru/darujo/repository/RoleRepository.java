package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Project;
import ru.darujo.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<@NonNull Role, @NonNull Long>, JpaSpecificationExecutor<@NonNull Role> {
    Optional<Role> findByNameIgnoreCaseAndProject(String roleName, Project project);

    Optional<Role> findByNameIgnoreCaseAndIdIsNotAndProject(String name, long id, Project project);
}
