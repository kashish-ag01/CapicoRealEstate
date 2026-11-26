package com.capico.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final String jwtSecret;
    private final long jwtExpiration;

    public JwtService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(
            Long userId,
            String email,
            String role
    ) {
        Map<String, Object> claims = Map.of(
                "userId", userId,
                "role", normalizeRole(role)
        );

        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + jwtExpiration
        );

        return Jwts.builder()
                .claims(claims)
                .id(UUID.randomUUID().toString())
                .subject(email)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    public Long extractUserId(String token) {
        Number userId = extractAllClaims(token)
                .get("userId", Number.class);

        if (userId == null) {
            throw new IllegalArgumentException(
                    "JWT does not contain a userId"
            );
        }

        return userId.longValue();
    }

    public String extractRole(String token) {
        String role = extractAllClaims(token)
                .get("role", String.class);

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT does not contain a role"
            );
        }

        return normalizeRole(role);
    }

    public String extractTokenId(String token) {
        String tokenId = extractClaim(
                token,
                Claims::getId
        );

        if (tokenId == null || tokenId.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT does not contain a token ID"
            );
        }

        return tokenId;
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(
                token,
                Claims::getIssuedAt
        );
    }

    public Date extractExpiration(String token) {
        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    public boolean isTokenValid(
            String token,
            String expectedEmail
    ) {
        String tokenEmail = extractEmail(token);

        return tokenEmail != null
                && tokenEmail.equalsIgnoreCase(expectedEmail)
                && !isTokenExpired(token);
    }

    public long getRemainingValidityInMilliseconds(
            String token
    ) {
        long remaining =
                extractExpiration(token).getTime()
                        - System.currentTimeMillis();

        return Math.max(remaining, 0);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date());
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Role cannot be empty"
            );
        }

        return role.startsWith("ROLE_")
                ? role.substring(5)
                : role;
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes =
                Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}