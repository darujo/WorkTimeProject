package ru.darujo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long>, JpaSpecificationExecutor<User> {
    public Optional<User> findByNikNameIgnoreCase(String nikName);
}
