package com.sys.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ===============================
            // 🔓 CORS
            // ===============================
            .cors(cors -> {})

            // ===============================
            // 🔓 CSRF OFF (API REST / WS)
            // ===============================
            .csrf(csrf -> csrf.disable())

            // ===============================
            // 🔓 Stateless (JWT / API)
            // ===============================
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // ===============================
            // 🔓 RUTAS PUBLICAS
            // ===============================
            .authorizeHttpRequests(auth -> auth

                // AUTH
                .requestMatchers("/api/auth/**")
                .permitAll()

                // 📺 HORARIOS (TV pública)
                .requestMatchers("/api/horaries/**")
                .permitAll()

                // 🔌 WEBSOCKET
                .requestMatchers("/ws-horarios/**")
                .permitAll()

                .requestMatchers("/topic/**")
                .permitAll()

                .requestMatchers("/app/**")
                .permitAll()

                // 📄 Swagger
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()

                // 🔒 Todo lo demás protegido
                .anyRequest().authenticated()
            )

            // ===============================
            // 🔓 Sin login HTML
            // ===============================
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ===============================
    // 🔐 Encoder
    // ===============================
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
