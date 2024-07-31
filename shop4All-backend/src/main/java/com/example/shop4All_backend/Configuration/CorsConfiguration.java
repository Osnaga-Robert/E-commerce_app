package com.example.shop4All_backend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    // Configure CORS settings for the application
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow CORS requests to any endpoint
                registry.addMapping("/**")
                        // Allow GET, POST, PUT, and DELETE methods
                        .allowedMethods(GET, POST, PUT, DELETE)
                        // Allow all headers
                        .allowedHeaders("*")
                        // Allow all origin patterns
                        .allowedOriginPatterns("*")
                        // Allow credentials (cookies, authorization headers, etc.)
                        .allowCredentials(true)
                ;
            }
        };
    }
}
