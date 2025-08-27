package com.samha.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        SamhaAuthenticationFilter authenticationFilter = new SamhaAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    new AntPathRequestMatcher("/*"),
                    new AntPathRequestMatcher("/assets/**"),
                    new AntPathRequestMatcher("/api/login/**"),
                    new AntPathRequestMatcher("/api/auth/**"),
                    new AntPathRequestMatcher("/api/public/**")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/**", "GET")).hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO")
                .requestMatchers(new AntPathRequestMatcher("/api/**", "POST")).hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO")
                .requestMatchers(new AntPathRequestMatcher("/api/**", "PATCH")).hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO")
            );

        return http.build();
    }
}