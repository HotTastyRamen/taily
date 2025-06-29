package dev.HTR.repositories;

import dev.HTR.entities.auth.AuthUserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepo
        extends PagingAndSortingRepository<AuthUserEntity, Long> {

    Page<AuthUserEntity> findAll( Pageable pageable );

    Optional<AuthUserEntity> findByUsername (String username);

    Optional<AuthUserEntity> findById (Long id);

    AuthUserEntity save (AuthUserEntity authUserEntity);
}
