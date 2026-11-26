package com.capico.user.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public registration
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        ).permitAll()

                        // Public login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/login"
                        ).permitAll()

                        // Public health check
                        .requestMatchers(
                                "/actuator/health"
                        ).permitAll()

                        // Logged-in user can retrieve own profile
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/me"
                        ).authenticated()

                        // Logged-in user can change own password
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/users/me/password"
                        ).authenticated()

                        // Logged-in user can request profile update.
                        // Ownership is checked inside UserService.
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/*"
                        ).authenticated()

                        // Only ADMIN can retrieve all users


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/internal/*"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users"
                        ).hasRole("ADMIN")

                        // Only ADMIN can retrieve another user by ID
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/*"
                        ).hasRole("ADMIN")

                        // Only ADMIN can delete users
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        // Explicit admin endpoints
                        .requestMatchers(
                                "/api/users/admin/**"
                        ).hasRole("ADMIN")

                        // Other endpoints require a valid JWT
                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception -> exception

                        // Missing or invalid authentication
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );
                                    response.setContentType(
                                            "application/json"
                                    );
                                    response.getWriter().write("""
                                            {
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Authentication is required"
                                            }
                                            """);
                                }
                        )

                        // Authenticated but insufficient role
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );
                                    response.setContentType(
                                            "application/json"
                                    );
                                    response.getWriter().write("""
                                            {
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "You do not have permission to perform this operation"
                                            }
                                            """);
                                }
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}