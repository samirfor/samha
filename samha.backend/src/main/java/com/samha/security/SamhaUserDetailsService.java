package com.samha.security;


import com.samha.domain.Usuario;
import com.samha.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class SamhaUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final IUsuarioService usuarioService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByLogin(username);
        if(usuario == null){
            throw new UsernameNotFoundException("Usuario não encontrado");
        }else{
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(usuario.getPapel().getNome()));
            return new User(usuario.getLogin(), usuario.getSenha(), authorities);
        }
    }
}
