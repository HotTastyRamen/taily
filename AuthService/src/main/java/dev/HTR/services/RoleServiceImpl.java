package dev.HTR.services;

import dev.HTR.entities.auth.AuthUserEntity;
import dev.HTR.entities.auth.Role;
import dev.HTR.repositories.AuthUserRepo;
import dev.HTR.repositories.RoleRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepo roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    public Set<Role> getDefaultRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(findByName("ROLE_USER")); // имя должно соответствовать значению в БД
        return roles;
    }

    @Transactional
    public void assignRolesToUser(AuthUserEntity user, Set<Role> roles) {
        user.setRoles(roles);
    }

    @Transactional
    public void assignDefaultRolesToUser(AuthUserEntity user) {
        user.setRoles(getDefaultRoles());

    }
}
