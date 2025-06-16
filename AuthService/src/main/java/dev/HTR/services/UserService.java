package dev.HTR.services;

import dev.HTR.DTOs.UserDTO;
import dev.HTR.repositories.AuthUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private AuthUserRepo authUserRepo;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return authUserRepo.findAll(pageable)
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    return dto;
                });
    }
}
