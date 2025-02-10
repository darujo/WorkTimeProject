package ru.darujo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.darujo.model.Role;
@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
}
