package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.ServiceModel;

import java.util.Optional;


@Repository
@Primary
public interface ServiceModelRepository extends CrudRepository<@NonNull ServiceModel, @NonNull Long>, JpaSpecificationExecutor<@NonNull ServiceModel> {
    Optional<ServiceModel> findByNameIgnoreCase(String name);
}
