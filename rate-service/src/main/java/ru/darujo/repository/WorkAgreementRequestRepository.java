package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.WorkAgreementRequest;

@Repository
@Primary
public interface WorkAgreementRequestRepository extends CrudRepository<@NonNull WorkAgreementRequest, @NonNull Long>, JpaSpecificationExecutor<@NonNull WorkAgreementRequest> {
}
