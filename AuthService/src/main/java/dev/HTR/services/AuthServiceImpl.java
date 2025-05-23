package dev.HTR.services;

import dev.HTR.DTOs.RegisterRequest;
import dev.HTR.entities.auth.AuthUserEntity;
import dev.HTR.repositories.AuthUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService {

    @Autowired
    private final AuthUserRepo authUserRepo;

    @Autowired
    private final RoleService roleService;

    public Optional<AuthUserEntity> findByUsername(String username){
        return authUserRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserEntity user = findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }


}
