package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.ServiceStatus;

import java.util.Optional;

@Repository
@Primary
public interface ServiceStatusRepository extends CrudRepository<@NonNull ServiceStatus, @NonNull Long>, JpaSpecificationExecutor<@NonNull ServiceStatus> {
    Optional<ServiceStatus> findDistinctTopByOrderByIdDesc();
}
