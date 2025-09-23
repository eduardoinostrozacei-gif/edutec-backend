package com.edutec.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authProvider(UserDetailsService uds, PasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    private final AuthenticationEntryPoint unauthorized = (req, res, ex) -> {
        res.setStatus(401);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\":\"unauthorized\"}");
    };

    @Bean
    SecurityFilterChain filter(HttpSecurity http,
                               AuthenticationProvider ap) throws Exception {
        http
                .cors(c -> {}) // si tienes CorsConfig, se aplica aquí
                .csrf(cs -> cs.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorized))
                .authorizeHttpRequests(reg -> reg
                        // preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // público: salud/diagnóstico (útil en deploy)
                        .requestMatchers("/actuator/**").permitAll()

                        // público: login/registro
                        .requestMatchers("/api/auth/**").permitAll()

                        // público: endpoints abiertos (p.ej., /api/public/ping)
                        .requestMatchers("/api/public/**").permitAll()

                        // todo lo demás, protegido
                        .anyRequest().authenticated()
                )
                .authenticationProvider(ap);

        return http.build();
    }
}
