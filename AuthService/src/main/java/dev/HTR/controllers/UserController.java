package dev.HTR.controllers;


import dev.HTR.DTOs.JwtRequest;
import dev.HTR.DTOs.JwtResponse;
import dev.HTR.services.AuthService;
import dev.HTR.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final AuthService authService;

    @PostMapping("/user")
    public ResponseEntity<?> getUser(@RequestBody String username){

        return ResponseEntity.ok(authService.findByUsername(username));

    }
}
