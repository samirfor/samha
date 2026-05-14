package com.samha.bootstrap;

import com.samha.domain.Papel;
import com.samha.domain.Usuario;
import com.samha.persistence.IPapelRepository;
import com.samha.persistence.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final AdminBootstrapProperties properties;
    private final IUsuarioRepository usuarioRepository;
    private final IPapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrapRunner(AdminBootstrapProperties properties,
                                IUsuarioRepository usuarioRepository,
                                IPapelRepository papelRepository,
                                PasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }

        if (!StringUtils.hasText(properties.getLogin())
                || !StringUtils.hasText(properties.getPassword())
                || !StringUtils.hasText(properties.getPapelNome())) {
            throw new IllegalStateException("Admin bootstrap enabled, but login, password or papelNome is blank");
        }

        Papel papel = papelRepository.findByNome(properties.getPapelNome());
        if (papel == null) {
            throw new IllegalStateException("Role not found for admin bootstrap: " + properties.getPapelNome());
        }

        Usuario usuario = usuarioRepository.findByLogin(properties.getLogin());
        if (usuario == null) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setLogin(properties.getLogin());
            novoUsuario.setSenha(passwordEncoder.encode(properties.getPassword()));
            novoUsuario.setPapel(papel);
            usuarioRepository.save(novoUsuario);
            log.info("Admin user '{}' created with role '{}'", properties.getLogin(), properties.getPapelNome());
            return;
        }

        boolean updated = false;
        if (usuario.getPapel() == null || !properties.getPapelNome().equals(usuario.getPapel().getNome())) {
            usuario.setPapel(papel);
            updated = true;
        }

        if (!StringUtils.hasText(usuario.getSenha()) || !passwordEncoder.matches(properties.getPassword(), usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(properties.getPassword()));
            updated = true;
        }

        if (updated) {
            usuarioRepository.save(usuario);
            log.info("Admin user '{}' updated from compose bootstrap", properties.getLogin());
        } else {
            log.info("Admin user '{}' already aligned with compose bootstrap", properties.getLogin());
        }
    }
}
