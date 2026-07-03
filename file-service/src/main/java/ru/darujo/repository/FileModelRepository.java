package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.FileModel;

public interface FileModelRepository extends CrudRepository<@NonNull FileModel, @NonNull Long>, JpaSpecificationExecutor<@NonNull FileModel> {
}