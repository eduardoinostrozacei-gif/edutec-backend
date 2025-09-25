package com.edutec.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    // Fuente CORS que Spring Security usa cuando haces http.cors(...)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Orígenes permitidos (agrega aquí los que uses en local)
        cfg.setAllowedOrigins(List.of(
                "https://edutec-frontend.onrender.com",
                "http://localhost:4200",
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        // Métodos y headers
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With","Accept","Origin"));

        // Headers que el navegador puede leer en la respuesta
        cfg.setExposedHeaders(List.of("Authorization","Location"));

        // Si usas cookies/sesión, mantenlo true; para JWT no molesta.
        cfg.setAllowCredentials(true);

        // Cache del preflight (en segundos)
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
