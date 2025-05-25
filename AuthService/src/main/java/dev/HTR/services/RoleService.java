package dev.HTR.services;

import dev.HTR.entities.auth.AuthUserEntity;

public interface RoleService {
    void assignDefaultRolesToUser(AuthUserEntity authUserEntity);
}
