package com.samha.application.token;

import com.samha.commons.UseCase;
import com.samha.domain.Usuario;
import com.samha.service.IUsuarioService;

import javax.inject.Inject;
import java.util.Map;

public class ChangePassword extends UseCase<Void> {

    private Map<String, String> params;

    @Inject
    public ChangePassword(Map<String, String> params) {
        this.params = params;
    }

    @Inject
    private IUsuarioService usuarioService;

    @Override
    protected Void execute() throws Exception {
        String login = params.get("login");
        String senha = params.get("senha");
        Usuario user = usuarioService.findByLogin(login);
        user.setSenha(senha);
        usuarioService.saveUsuario(user);
        return null;
    }
}
