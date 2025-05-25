package dev.HTR.repositories;

import dev.HTR.entities.auth.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegesRepo
        extends CrudRepository<Privilege, Long> {
}
