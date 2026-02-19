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
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ✅ PERMITIR PREFLIGHT
                        .requestMatchers(
                                org.springframework.http.HttpMethod.OPTIONS,
                                "/**")
                        .permitAll()

                        // AUTH
                        .requestMatchers("/api/auth/**").permitAll()

                        // HORARIOS
                        .requestMatchers("/api/horaries/**").permitAll()

                        // WEBSOCKET
                        .requestMatchers("/ws-horarios/**").permitAll()
                        .requestMatchers("/topic/**").permitAll()
                        .requestMatchers("/app/**").permitAll()

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // ✅ ENDPOINT DE SALUD PARA CRON-JOB
                        .requestMatchers("/ping").permitAll()

                        .anyRequest().authenticated())

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
