package dev.HTR.controllers;


import dev.HTR.DTOs.JwtRequest;
import dev.HTR.DTOs.JwtResponse;
import dev.HTR.DTOs.UserDTO;
import dev.HTR.DTOs.UserInfoDto;
import dev.HTR.repositories.AuthUserRepo;
import dev.HTR.services.AuthService;
import dev.HTR.services.UserService;
import dev.HTR.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final AuthUserRepo authUserRepo;

    @GetMapping("/byName")
    public ResponseEntity<?> getUser(@RequestBody String username){
        return ResponseEntity.ok(authService.findByUsername(username));
    }

    @GetMapping
    public Page<UserDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}/phone")
    public String getUserPhoneNumber(@PathVariable Long id) {
        return authService.getPhoneNumberById(id);
    }

    @GetMapping("/myName")
    public ResponseEntity<UserInfoDto> getName(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        Long userId = authUserRepo.findByUsername(username).get().getId();
        UserInfoDto userInfo = new UserInfoDto(username, userId);
        return ResponseEntity.ok(userInfo);
    }
}
