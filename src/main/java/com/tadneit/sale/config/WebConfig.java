package com.tadneit.sale.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (profile.equals("local")) {
                    registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*")
                            .exposedHeaders("Content-Disposition");
                } else if (profile.equals("dev")) {
                    registry.addMapping("/**")
                            .allowedOrigins("https://your-production-domain.com")
                            .allowedMethods("GET", "POST", "PUT", "DELETE")
                            .allowedHeaders("*");
                }
            }
        };
    }
}
