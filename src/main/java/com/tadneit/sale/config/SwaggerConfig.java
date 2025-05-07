package com.tadneit.sale.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
@OpenAPIDefinition(
        info = @Info(
                title = "TadNeit Sale Project",
                description = "TadNeit Sale Project 2025",
                version = "a0.0.1",
                contact = @Contact(name = "", url = "", email = "")
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addResponses("200", createApiResponse("OK"))
                        .addResponses("403", createApiResponse("Forbidden"))
                        .addResponses("401", createApiResponse("Unauthorized"))
                        .addResponses("400", createApiResponse("Bad Request"))
                        .addResponses("500", createApiResponse("Internal Server Error")));
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }
}