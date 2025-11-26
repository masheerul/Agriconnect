package com.example.agriconnect.jwtutil;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtil {

    private final String SECRET = "ThisIsASecretKeyForJwtAuthentication1234567890";
    private final long EXPIRATION = 1000 * 60 * 60 * 10; 

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    
    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .setSubject(email) 
                .claim("id", userId) 
                .claim("role", role) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return getAllClaims(token).get("id", Long.class);
    }

    public String extractRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }
    
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}


