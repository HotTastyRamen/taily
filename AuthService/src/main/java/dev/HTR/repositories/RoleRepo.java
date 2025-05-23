package dev.HTR.repositories;

import dev.HTR.entities.auth.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
}
