package com.samha.application.token;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.util.JWTUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RefreshSession extends UseCase<Void> {

    private final HttpServletResponse response;
    private final HttpServletRequest request;

    public RefreshSession(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    @Override
    protected Void execute() throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                JWTUtil.verifyToken(token);
                String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                JWTUtil.refreshSession(user, authorities, request, response);
            } catch (TokenExpiredException ex) {
                throw new BusinessException("Sua sessão expirou! Faça login novamente.");
            } catch (Exception ex) {
                JWTUtil.writeErrorResponse(response, ex);
            }
        } else {
            throw new BusinessException("Não foi possível renovar a sessão do usuário.");
        }
        return null;
    }
}
