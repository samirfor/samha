package com.samha.security;

import com.samha.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SamhaAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/auth/refreshToken")){
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                String token = authorizationHeader.substring("Bearer ".length());
                JWTUtil.verifyToken(token, response);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
