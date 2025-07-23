package com.samha.service;


import com.samha.domain.Usuario;

public interface IUsuarioService {
    Usuario saveUsuario(Usuario usuario);
    Usuario findByLogin(String login);
}
