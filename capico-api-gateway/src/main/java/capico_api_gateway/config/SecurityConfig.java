package capico_api_gateway.config;

import capico_api_gateway.security.JwtAuthenticationFilter;
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
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

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

                        // Public system endpoints
                        .requestMatchers(
                                "/actuator/health",
                                "/error"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/me"
                        ).authenticated()

                        /*
                         * ADMIN user management
                         */

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/{id}"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        /*
                         * Authenticated user operations
                         */



                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/users/me/password"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/*"
                        ).authenticated()

                        /*
                         * Property viewing is available
                         * to both ADMIN and USER.
                         */

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/**"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        /*
                         * Property management is ADMIN-only
                         * according to your case study.
                         */

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/properties/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/properties/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/**"
                        ).hasRole("ADMIN")

                        /*
                         * Property images:
                         * USER and ADMIN can view.
                         */

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/property-images/**"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        /*
                         * Only ADMIN manages property images.
                         */

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/property-images/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/property-images/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/property-images/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/property-images/**"
                        ).hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request,
                                 response,
                                 authException) -> {

                                    response.setStatus(
                                            HttpServletResponse
                                                    .SC_UNAUTHORIZED
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

                        .accessDeniedHandler(
                                (request,
                                 response,
                                 accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse
                                                    .SC_FORBIDDEN
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