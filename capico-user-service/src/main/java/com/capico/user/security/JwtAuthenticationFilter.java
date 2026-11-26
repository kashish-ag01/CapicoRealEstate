package com.capico.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.capico.user.repository.RevokedTokenRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            RevokedTokenRepository revokedTokenRepository,
            ObjectMapper objectMapper
    ) {
        this.jwtService = jwtService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader
                .substring(7)
                .trim();

        if (token.isBlank()) {
            writeUnauthorizedResponse(
                    request,
                    response,
                    "Bearer token is missing"
            );
            return;
        }

        try {
            String tokenId = jwtService.extractTokenId(token);

            if (revokedTokenRepository.existsByTokenId(tokenId)) {
                SecurityContextHolder.clearContext();

                writeUnauthorizedResponse(
                        request,
                        response,
                        "JWT has been logged out"
                );

                return;
            }
            String email = jwtService.extractEmail(token);

            if (email != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null
                    && jwtService.isTokenValid(token, email)) {

                Long userId =
                        jwtService.extractUserId(token);

                String role =
                        jwtService.extractRole(token);

                String authorityName =
                        role.startsWith("ROLE_")
                                ? role
                                : "ROLE_" + role;

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                authorityName
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(authority)
                        );

                Map<String, Object> authenticationDetails =
                        new LinkedHashMap<>();

                authenticationDetails.put(
                        "userId",
                        userId
                );

                authenticationDetails.put(
                        "email",
                        email
                );

                authenticationDetails.put(
                        "role",
                        role
                );

                authenticationDetails.put(
                        "tokenId",
                        jwtService.extractTokenId(token)
                );

                authentication.setDetails(
                        authenticationDetails
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();

            writeUnauthorizedResponse(
                    request,
                    response,
                    "Invalid or expired JWT"
            );
        }
    }

    private void writeUnauthorizedResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse =
                new LinkedHashMap<>();

        errorResponse.put(
                "timestamp",
                LocalDateTime.now()
        );

        errorResponse.put(
                "status",
                HttpServletResponse.SC_UNAUTHORIZED
        );

        errorResponse.put(
                "error",
                "Unauthorized"
        );

        errorResponse.put(
                "message",
                message
        );

        errorResponse.put(
                "path",
                request.getRequestURI()
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse
        );
    }
}