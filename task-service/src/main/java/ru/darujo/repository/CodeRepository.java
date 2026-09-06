package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.Code;

public interface CodeRepository extends CrudRepository<@NonNull Code, @NonNull Long>, JpaSpecificationExecutor<@NonNull Code> {
}