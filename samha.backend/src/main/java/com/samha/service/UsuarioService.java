package com.samha.service;


import com.samha.domain.Usuario;
import com.samha.persistence.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements IUsuarioService{

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        Usuario bancoUser = usuarioRepository.findByLogin(usuario.getLogin());
        if (bancoUser != null) {
            String novaSenha = passwordEncoder.encode(usuario.getSenha());
            if(!bancoUser.getSenha().equals(novaSenha)) bancoUser.setSenha(novaSenha);
        } else {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }
}
