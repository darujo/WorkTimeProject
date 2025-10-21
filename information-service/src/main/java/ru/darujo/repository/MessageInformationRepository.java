package ru.darujo.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.MessageInformation;

@Repository
@Primary
public interface MessageInformationRepository extends CrudRepository<MessageInformation,Long>, JpaSpecificationExecutor<MessageInformation> {
}
