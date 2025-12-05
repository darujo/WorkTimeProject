package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Release;

import java.util.Optional;

@Repository
@Primary
public interface ReleaseRepository extends CrudRepository<@NonNull Release,@NonNull Long>, JpaSpecificationExecutor<@NonNull Release> {
    Optional<Release> findByNameIgnoreCase(String name);
}
