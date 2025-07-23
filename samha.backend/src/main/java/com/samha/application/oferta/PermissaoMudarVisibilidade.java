package com.samha.application.oferta;

import com.samha.commons.UseCase;
import com.samha.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class PermissaoMudarVisibilidade extends UseCase<Boolean> {

    private final HttpServletRequest request;

    public PermissaoMudarVisibilidade(HttpServletRequest request) {
        this.request = request;
    }
    @Override
    protected Boolean execute() throws Exception {
        String access_token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        List<String> papeis = JWTUtil.getPapeisFromToken(access_token);
        if (papeis.contains("COORDENADOR_ACADEMICO")) return true;
        return false;
    }
}
