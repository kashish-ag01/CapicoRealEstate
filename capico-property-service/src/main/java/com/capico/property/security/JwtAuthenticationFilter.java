package com.capico.property.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomAuthenticatorEntryPoint
            authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomAuthenticatorEntryPoint authenticatorEntryPoint
    ) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint =
                authenticatorEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader(
                        HttpHeaders.AUTHORIZATION
                );

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(
                "Bearer "
        )) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authorizationHeader.substring(7);

        if (token.isBlank()) {
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new org.springframework.security
                            .authentication
                            .BadCredentialsException(
                            "JWT token is missing"
                    )
            );

            return;
        }

        try {
            Claims claims =
                    jwtService.extractClaims(token);

            String username =
                    jwtService.extractUsername(claims);

            Long userId =
                    jwtService.extractUserId(claims);

            String role =
                    jwtService.extractRole(claims);

            if (username == null
                    || username.isBlank()
                    || role == null
                    || role.isBlank()) {

                authenticationEntryPoint.commence(
                        request,
                        response,
                        new org.springframework.security
                                .authentication
                                .BadCredentialsException(
                                "JWT token does not contain required claims"
                        )
                );

                return;
            }

            AuthenticatedUser authenticatedUser =
                    new AuthenticatedUser(
                            userId,
                            username,
                            role
                    );

            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority(
                            "ROLE_" + role
                    );

            UsernamePasswordAuthenticationToken
                    authentication =
                    new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            null,
                            List.of(authority)
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (JwtException
                 | IllegalArgumentException exception) {

            SecurityContextHolder.clearContext();

            authenticationEntryPoint.commence(
                    request,
                    response,
                    new org.springframework.security
                            .authentication
                            .BadCredentialsException(
                            "Invalid or expired JWT token",
                            exception
                    )
            );
        }
    }
}