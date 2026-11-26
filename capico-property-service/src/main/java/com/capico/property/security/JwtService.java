package com.capico.property.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        byte[] keyBytes =
                Decoders.BASE64.decode(jwtSecret);

        this.signingKey =
                Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(
            String token
    ) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(
            String token
    ) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException
                 | IllegalArgumentException exception) {
            return false;
        }
    }

    public String extractUsername(
            Claims claims
    ) {
        return claims.getSubject();
    }

    public Long extractUserId(
            Claims claims
    ) {
        Object userIdClaim =
                claims.get("userId");

        if (userIdClaim == null) {
            return null;
        }

        if (userIdClaim instanceof Number number) {
            return number.longValue();
        }

        try {
            return Long.valueOf(
                    userIdClaim.toString()
            );
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public String extractRole(
            Claims claims
    ) {
        Object roleClaim =
                claims.get("role");

        if (roleClaim == null) {
            return null;
        }

        String role =
                roleClaim.toString()
                        .trim()
                        .toUpperCase();

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        return role;
    }
}