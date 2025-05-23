package dev.HTR.repositories;

import dev.HTR.entities.auth.AuthUserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepo extends PagingAndSortingRepository<AuthUserEntity, Long> {

    Optional<Iterable<AuthUserEntity>> findAll(Example<AuthUserEntity> example, Pageable pageable );

    Optional<AuthUserEntity> findByUsername (String username);
}
