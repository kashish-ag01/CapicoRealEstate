package capico_api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean isRegistration =
                "POST".equalsIgnoreCase(method)
                        && "/api/users".equals(path);

        boolean isLogin =
                "POST".equalsIgnoreCase(method)
                        && "/api/users/login".equals(path);

        boolean isPublicSystemEndpoint =
                "/error".equals(path)
                        || path.startsWith("/actuator/");

        return isRegistration
                || isLogin
                || isPublicSystemEndpoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null
                || !authorizationHeader.startsWith(BEARER_PREFIX)) {

            sendUnauthorized(
                    response,
                    "Bearer token is required"
            );

            return;
        }

        String token = authorizationHeader
                .substring(BEARER_PREFIX.length())
                .trim();

        if (token.isBlank()) {
            sendUnauthorized(
                    response,
                    "Bearer token cannot be empty"
            );

            return;
        }

        try {
            if (!jwtService.isTokenValid(token)) {
                sendUnauthorized(
                        response,
                        "Invalid or expired JWT token"
                );

                return;
            }

            String email = jwtService.extractEmail(token);
            Long userId = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token);

            String authorityName = role.startsWith("ROLE_")
                    ? role
                    : "ROLE_" + role;

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            authorityName
                                    )
                            )
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            Map<String, String> trustedHeaders =
                    new HashMap<>();

            trustedHeaders.put(
                    "X-User-Id",
                    String.valueOf(userId)
            );

            trustedHeaders.put(
                    "X-User-Email",
                    email
            );

            trustedHeaders.put(
                    "X-User-Role",
                    role.replace("ROLE_", "")
            );

            HttpServletRequest wrappedRequest =
                    new TrustedHeaderRequestWrapper(
                            request,
                            trustedHeaders
                    );

            /*
             * Keep this outside JWT parsing logic.
             * Downstream errors must remain downstream errors.
             */
            filterChain.doFilter(
                    wrappedRequest,
                    response
            );

        } catch (Exception exception) {
            SecurityContextHolder.clearContext();

            sendUnauthorized(
                    response,
                    "Invalid or expired JWT token"
            );
        }
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        response.getWriter().write("""
                {
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "%s"
                }
                """.formatted(message));
    }

    private static class TrustedHeaderRequestWrapper
            extends HttpServletRequestWrapper {

        private final Map<String, String> trustedHeaders;

        public TrustedHeaderRequestWrapper(
                HttpServletRequest request,
                Map<String, String> trustedHeaders
        ) {
            super(request);
            this.trustedHeaders =
                    new HashMap<>(trustedHeaders);
        }

        @Override
        public String getHeader(String name) {

            String value = trustedHeaders.get(name);

            if (value != null) {
                return value;
            }

            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(
                String name
        ) {
            String value = trustedHeaders.get(name);

            if (value != null) {
                return Collections.enumeration(
                        Collections.singletonList(value)
                );
            }

            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {

            Vector<String> names = new Vector<>();

            Enumeration<String> existingNames =
                    super.getHeaderNames();

            while (existingNames.hasMoreElements()) {
                names.add(existingNames.nextElement());
            }

            for (String trustedHeader
                    : trustedHeaders.keySet()) {

                if (!names.contains(trustedHeader)) {
                    names.add(trustedHeader);
                }
            }

            return names.elements();
        }
    }
}