package dev.HTR.controllers;


import dev.HTR.DTOs.JwtRequest;
import dev.HTR.DTOs.JwtResponse;
import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.DTOs.RegisterRequest;
import dev.HTR.entities.auth.AuthUserEntity;
import dev.HTR.services.AuthService;
import dev.HTR.services.KafkaProducerService;
import dev.HTR.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService producerService;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest authReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())
        );

        // Получаем пользователя из базы, чтобы отдать userId
        AuthUserEntity user = authService.findByUsername(authReq.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Long> response = new HashMap<>();
        response.put("userId", user.getId());

        NotificationEvent event = new NotificationEvent(user.getId(), "verification", user.getPhoneNumber());

        //Отправить кафка ивент в нотификации
        producerService.sendMessage(event);

        return ResponseEntity.ok(response);
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(
            @RequestBody JwtRequest authReq,
            @RequestHeader("Authorization") String verifyToken_untrim ){

        String verifyToken = extractToken(verifyToken_untrim);
        System.out.println(verifyToken);
        System.out.println(authReq.getUsername() + " " + jwtUtils.getUsernameFromVerificationToken(verifyToken));

        AuthUserEntity user = authService.findById(
                Long.parseLong(jwtUtils.getUsernameFromVerificationToken(verifyToken)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Objects.equals(authReq.getUsername(), user.getUsername())){

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( authReq.getUsername(), authReq.getPassword()));
            UserDetails userDetails = authService.loadUserByUsername(authReq.getUsername());

            String token = jwtUtils.generateAuthToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        }

        return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/loginCookie")
    public ResponseEntity<?> createAuthTokenCookie(
            @RequestBody JwtRequest authReq,
            @RequestHeader("Authorization") String verifyToken_untrim,
            HttpServletResponse response){

        String verifyToken = extractToken(verifyToken_untrim);
        System.out.println(verifyToken);
        System.out.println(authReq.getUsername() + " " + jwtUtils.getUsernameFromVerificationToken(verifyToken));

        AuthUserEntity user = authService.findById(
                        Long.parseLong(jwtUtils.getUsernameFromVerificationToken(verifyToken)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Objects.equals(authReq.getUsername(), user.getUsername())){

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( authReq.getUsername(), authReq.getPassword()));
            UserDetails userDetails = authService.loadUserByUsername(authReq.getUsername());

            String token = jwtUtils.generateAuthToken(userDetails);

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true) // только по HTTPS
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Strict") // или Lax
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok().build();
        }

        return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
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

    @GetMapping("/auth/check")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        // Пример: если фильтр безопасности уже проверил JWT и пользователь аутентифицирован
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);

        return ResponseEntity.ok(Map.of("authenticated", isAuthenticated));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true) // только если используешь HTTPS
                .path("/")
                .maxAge(0) // удаляет cookie
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }


}
