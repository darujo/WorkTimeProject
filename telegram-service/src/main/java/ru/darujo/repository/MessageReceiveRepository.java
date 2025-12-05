package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.MessageReceive;

@Repository
@Primary
public interface MessageReceiveRepository extends CrudRepository<@NonNull MessageReceive, @NonNull Long>, JpaSpecificationExecutor<@NonNull MessageReceive> {

}
