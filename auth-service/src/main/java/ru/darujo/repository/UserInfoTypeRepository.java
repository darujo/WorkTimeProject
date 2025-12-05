package ru.darujo.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;

import java.util.Optional;

@Repository
public interface UserInfoTypeRepository extends CrudRepository<@NonNull UserInfoType, @NonNull Long>, JpaSpecificationExecutor<@NonNull UserInfoType> {
    Optional<UserInfoType> findFirstByCodeAndUser(String code, User user);
}
