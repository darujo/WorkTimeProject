package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Work;

@Repository
@Primary
public interface WorkRepository extends CrudRepository<@NonNull Work, @NonNull Long>, JpaSpecificationExecutor<@NonNull Work> {

}
