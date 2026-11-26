package capico_api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        String email = extractAllClaims(token).getSubject();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email subject is missing from JWT"
            );
        }

        return email;
    }

    public Long extractUserId(String token) {
        Number userId = extractAllClaims(token)
                .get("userId", Number.class);

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID claim is missing from JWT"
            );
        }

        return userId.longValue();
    }

    public String extractRole(String token) {
        String role = extractAllClaims(token)
                .get("role", String.class);

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Role claim is missing from JWT"
            );
        }

        return role;
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return claims.getSubject() != null
                    && claims.get("userId") != null
                    && claims.get("role") != null;

        } catch (Exception exception) {
            return false;
        }
    }
}
