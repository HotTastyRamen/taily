package dev.HTR.controllers;


import dev.HTR.DTOs.JwtRequest;
import dev.HTR.DTOs.JwtResponse;
import dev.HTR.DTOs.RegisterRequest;
import dev.HTR.entities.auth.AuthUserEntity;
import dev.HTR.services.AuthService;
import dev.HTR.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authReq){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken( authReq.getUsername(), authReq.getPassword()));
        UserDetails userDetails = authService.loadUserByUsername(authReq.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @PostMapping("/reg")
    public ResponseEntity<?> createNewUser (@RequestBody RegisterRequest registerRequest){
        AuthUserEntity reqUser = new AuthUserEntity(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                true
                );
        AuthUserEntity newUser = authService.createNewUser(reqUser);
        return ResponseEntity.ok(newUser);

    }
}
