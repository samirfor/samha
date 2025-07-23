package com.samha.application.token;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.Usuario;
import com.samha.service.IUsuarioService;
import com.samha.util.JWTUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.samha.util.JWTUtil.ACCESS_TOKEN_EXPIRATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class RefreshToken extends UseCase<Void> {

    private final HttpServletResponse response;
    private final HttpServletRequest request;

    @Inject
    public RefreshToken(HttpServletResponse response, HttpServletRequest request){
        this.request = request;
        this.response = response;
    }

    @Inject
    private IUsuarioService usuarioService;

    @Override
    protected Void execute() throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Refresh ")) {
            //caso o token ainda seja válido
            try {
                String token = authorizationHeader.substring("Refresh ".length());
                DecodedJWT decodedJWT = JWTUtil.verifyToken(token);
                String login = decodedJWT.getSubject();
                Usuario usuario = usuarioService.findByLogin(login);
                List<String> claims = new ArrayList<>();
                claims.add(usuario.getPapel().getNome());
                String access_token = JWTUtil.generateAccessToken(usuario.getLogin(), claims, request.getRequestURL().toString(), ACCESS_TOKEN_EXPIRATION);
                JWTUtil.writeTokenResponse(access_token, token, response);
            } catch (TokenExpiredException ex) { // caso ele já tenha expirado
                throw new BusinessException("Sua sessão expirou! Faça login novamente.");
            } catch (Exception ex) {
                JWTUtil.writeErrorResponse(response, ex);
            }
        } else {
            throw new BusinessException("Não foi possível encontrar o refresh token");
        }
        return null;
    }
}
