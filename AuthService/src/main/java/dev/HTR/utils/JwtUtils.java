package dev.HTR.utils;

import dev.HTR.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String authSecret;

    @Value("${jwt.verSecret}")
    private String verSecret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    @Autowired
    private AuthService authService;

    public String generateAuthToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roleList);
        claims.put("id", authService.findByUsername(userDetails.getUsername()).get().getId());

        Date issuedDate = new Date();
        Date expiredDate= new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, authSecret)
                .compact();
    }

    public String getUsernameFromAuthToken(String token) {
        return getAllClaimsFromJWT(token, authSecret).getSubject();
    }

    public String getUsernameFromVerificationToken(String token) {
        return getAllClaimsFromJWT(token, verSecret).getSubject();
    }

    public List<String> getRolesFromAuthToken(String token){
        return getAllClaimsFromJWT(token, authSecret).get("roles", List.class);
    }

    private Claims getAllClaimsFromJWT(String token, String secret){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
