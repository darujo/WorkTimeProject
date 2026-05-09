package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.DayInfo;

import java.time.LocalDate;

@Repository
public interface DayInfoRepository extends CrudRepository<@NonNull DayInfo, @NonNull Long>, JpaSpecificationExecutor<@NonNull DayInfo> {
    DayInfo findFirstByDate(LocalDate date);
}
