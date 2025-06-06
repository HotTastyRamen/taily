package dev.HTR.services;

import dev.HTR.entities.auth.AuthUserEntity;
import dev.HTR.exeptions.AlreadyExistsException;
import dev.HTR.repositories.AuthUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private final AuthUserRepo authUserRepo;

    private final RoleService roleService;

    public Optional<AuthUserEntity> findByUsername(String username){
        return authUserRepo.findByUsername(username);
    }

    public Optional<AuthUserEntity> findById(Long userId){
        return authUserRepo.findById(userId);
    }

    public AuthUserEntity save (AuthUserEntity authUserEntity){

        return authUserRepo.save(authUserEntity);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserEntity user = findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(
                        String.format("Пользователь '%s' не найден", username)
        ));

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    public AuthUserEntity createNewUser(AuthUserEntity authUserEntity){
        Optional<AuthUserEntity> existingUser = findByUsername(
                authUserEntity.getUsername());

        if (existingUser.isPresent()) {
            throw new AlreadyExistsException("User with username '"
                                            + authUserEntity.getUsername()
                                            + "' already exists.");
        }

        roleService.assignDefaultRolesToUser(authUserEntity);
        return save(authUserEntity);
    }
}
