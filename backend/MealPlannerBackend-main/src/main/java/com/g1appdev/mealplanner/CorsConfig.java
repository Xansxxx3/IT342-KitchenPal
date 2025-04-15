package com.g1appdev.mealplanner;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Allow Android Emulator (10.0.2.2) and Web App (localhost:3000 for React)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080",        // Localhost for the Spring Boot backend
            "http://10.0.2.2:8080",        // Emulator (Android Studio)
            "http://127.0.0.1:8080",       // Another common localhost IP
            "http://localhost:3000",        // Web app (React development server)
            "http://127.0.0.1:3000"        // Another localhost IP for React dev server
        ));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
