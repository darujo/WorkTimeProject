package ru.darujo.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Work;

@Repository
@Primary
public interface WorkRepository extends CrudRepository<Work,Long>, JpaSpecificationExecutor<Work> {

}
