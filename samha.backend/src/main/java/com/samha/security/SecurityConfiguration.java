package com.samha.security;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SamhaAuthenticationFilter authenticationFilter = new SamhaAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(management -> management.sessionCreationPolicy(STATELESS));
        http.authorizeRequests(requests -> requests.antMatchers(
                "/*",
                "/assets/**",
                "/api/login/**",
                "/api/auth/**",
                "/api/public/**"
        ).permitAll());
        http.authorizeRequests(requests -> requests.antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO"));
        http.authorizeRequests(requests -> requests.antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO"));
        http.authorizeRequests(requests -> requests.antMatchers(HttpMethod.PATCH, "/api/**").hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO"));
        http.authorizeRequests(requests -> requests.antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO"));
        http.authorizeRequests(requests -> requests.antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority("COORDENADOR_ACADEMICO", "COORDENADOR_CURSO"));
        http.authorizeRequests(requests -> requests.anyRequest().authenticated());
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new SamhaAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
