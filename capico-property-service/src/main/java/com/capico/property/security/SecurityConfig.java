package com.capico.property.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    private final CustomAuthenticatorEntryPoint
            authenticatorEntryPoint;

    private final CustomAccessDeniedHandler
            accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticatorEntryPoint authenticatorEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

        this.authenticatorEntryPoint =
                authenticatorEntryPoint;

        this.accessDeniedHandler =
                accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf ->
                        csrf.disable()
                )

                .formLogin(form ->
                        form.disable()
                )

                .httpBasic(basic ->
                        basic.disable()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(
                                        authenticatorEntryPoint
                                )
                                .accessDeniedHandler(
                                        accessDeniedHandler
                                )
                )

                .authorizeHttpRequests(authorize ->
                        authorize

                                // Service status endpoint
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/properties/status"
                                )
                                .permitAll()

                                // Swagger/OpenAPI, if used
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                )
                                .permitAll()

                                // View properties
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/properties",
                                        "/api/properties/**"
                                )
                                .authenticated()

                                // Add property
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/properties"
                                )
                                .hasRole("ADMIN")

                                // Update property
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/properties/**"
                                )
                                .hasRole("ADMIN")

                                // Manage property availability
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/properties/**"
                                )
                                .hasRole("ADMIN")

                                // Delete property
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/properties/**"
                                )
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}