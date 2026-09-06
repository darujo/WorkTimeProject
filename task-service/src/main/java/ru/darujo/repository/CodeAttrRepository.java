package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.CodeAttr;

public interface CodeAttrRepository extends CrudRepository<@NonNull CodeAttr, @NonNull Long>, JpaSpecificationExecutor<@NonNull CodeAttr> {
}