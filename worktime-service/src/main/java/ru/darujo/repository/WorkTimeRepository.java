package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.WorkTime;

public interface WorkTimeRepository extends CrudRepository<@NonNull WorkTime,@NonNull Long>, JpaSpecificationExecutor<@NonNull WorkTime> {
}