package dev.HTR.repositories;

import dev.HTR.entities.auth.Role;
import dev.HTR.entities.auth.enumes.Roles;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepo
        extends CrudRepository<Role, Long> {

    Optional<Role> findByName(Roles name);
}
