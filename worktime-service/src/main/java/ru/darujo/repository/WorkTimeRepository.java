package ru.darujo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.WorkTime;

public interface WorkTimeRepository extends CrudRepository<WorkTime,Long>, JpaSpecificationExecutor<WorkTime> {
}