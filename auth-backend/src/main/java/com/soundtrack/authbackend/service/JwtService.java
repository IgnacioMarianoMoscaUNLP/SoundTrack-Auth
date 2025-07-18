package com.soundtrack.authbackend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    public String generateToken(String sessionId) {
        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public String validateAndGetSessionId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
