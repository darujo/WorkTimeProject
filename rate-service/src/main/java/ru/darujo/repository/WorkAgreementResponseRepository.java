package ru.darujo.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkAgreementResponse;

@Repository
@Primary
public interface WorkAgreementResponseRepository extends CrudRepository<WorkAgreementResponse,Long>, JpaSpecificationExecutor<WorkAgreementResponse> {
}
