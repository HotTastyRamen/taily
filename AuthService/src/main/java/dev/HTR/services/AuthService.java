package dev.HTR.services;

import dev.HTR.entities.auth.AuthUserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthService {

    AuthUserEntity save(AuthUserEntity authUserEntity);

    UserDetails loadUserByUsername(String username);

    public String getPhoneNumberById(Long id);

    public Optional<AuthUserEntity> findById(Long userId);

    public Optional<AuthUserEntity> findByUsername(String username);

    public AuthUserEntity createNewUser(AuthUserEntity authUserEntity);

}
