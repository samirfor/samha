package com.samha.persistence;


import com.samha.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);
}
