package com.example.steplang.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secretString = "Test-String-To-Hash-Password-123-With-Long-Text";
    private final Key key = Keys.hmacShaKeyFor(secretString.getBytes());//Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email, long expirationMs){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ expirationMs))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }
}
