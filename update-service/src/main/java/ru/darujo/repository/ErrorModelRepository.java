package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.ErrorModel;

@Repository
@Primary
public interface ErrorModelRepository extends CrudRepository<@NonNull ErrorModel, @NonNull Long>, JpaSpecificationExecutor<@NonNull ErrorModel> {
}
