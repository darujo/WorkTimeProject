package ru.darujo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.darujo.model.WorkTime;

import java.util.Optional;

public interface WorkTimeRepository extends CrudRepository<WorkTime,Long>, JpaSpecificationExecutor<WorkTime> {
//    Optional<WorkTime> findFirstByTaskIdByOrderByWorkDateDesc(Long taskId);
}