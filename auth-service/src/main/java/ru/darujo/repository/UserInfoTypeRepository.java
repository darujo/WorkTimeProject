package ru.darujo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.User;
import ru.darujo.model.UserInfoType;

import java.util.Optional;

@Repository
public interface UserInfoTypeRepository extends CrudRepository<UserInfoType,Long>, JpaSpecificationExecutor<UserInfoType> {
    Optional<UserInfoType> findFirstByCodeAndUser(String code, User user);
}
