package dev.HTR.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String authSecret;

    public String getUsernameFromAuthToken(String token) {
        return getAllClaimsFromJWT(token, authSecret).getSubject();
    }


    public List<String> getRolesFromAuthToken(String token){
        return getAllClaimsFromJWT(token, authSecret).get("roles", List.class);
    }
    public Long getUserIdFromToken(String token) {
        return getAllClaimsFromJWT(token, authSecret).get("id", Long.class);
    }

    private Claims getAllClaimsFromJWT(String token, String secret){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
