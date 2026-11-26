package com.capico.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME =
            "bearerAuthentication";

    @Bean
    public OpenAPI userServiceOpenApi() {

        SecurityScheme bearerSecurityScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT");

        SecurityRequirement securityRequirement =
                new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME);

        Components components =
                new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                bearerSecurityScheme
                        );

        Info apiInformation =
                new Info()
                        .title("Capico User Service API")
                        .description(
                                "APIs for user registration, login, "
                                        + "profile management and administration"
                        )
                        .version("1.0.0");

        return new OpenAPI()
                .info(apiInformation)
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}