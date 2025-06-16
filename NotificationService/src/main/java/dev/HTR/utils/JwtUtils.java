package dev.HTR.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.verSecret}")
    private String secret;

    @Value("${jwt.verSecret}")
    private String authSecret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public String generateTokenWithUserId(Long Id) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setSubject(String.valueOf(Id))
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getUsernameFromAuthToken(String token) {
        return getAllClaimsFromAuthJWT(token, authSecret).getSubject();
    }

    public List<String> getRolesFromAuthToken(String token){
        return getAllClaimsFromAuthJWT(token, authSecret).get("roles", List.class);
    }

    private Claims getAllClaimsFromAuthJWT(String token, String secret){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
