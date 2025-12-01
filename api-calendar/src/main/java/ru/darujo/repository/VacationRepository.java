package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Vacation;

@Repository
public interface VacationRepository extends CrudRepository<@NonNull Vacation, @NonNull Long>, JpaSpecificationExecutor<@NonNull Vacation> {
}
