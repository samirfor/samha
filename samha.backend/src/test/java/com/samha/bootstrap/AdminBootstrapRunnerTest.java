package com.samha.bootstrap;

import com.samha.domain.Papel;
import com.samha.domain.Usuario;
import com.samha.persistence.IPapelRepository;
import com.samha.persistence.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminBootstrapRunnerTest {

    @Mock
    private AdminBootstrapProperties properties;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IPapelRepository papelRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationArguments applicationArguments;

    private AdminBootstrapRunner runner;

    @BeforeEach
    void setUp() {
        runner = new AdminBootstrapRunner(properties, usuarioRepository, papelRepository, passwordEncoder);
    }

    @Test
    void runDoesNothingWhenBootstrapIsDisabled() {
        when(properties.isEnabled()).thenReturn(false);

        runner.run(applicationArguments);

        verifyNoInteractions(usuarioRepository, papelRepository, passwordEncoder);
    }

    @Test
    void runThrowsWhenRequiredPropertiesAreBlank() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLogin()).thenReturn("");

        assertThatThrownBy(() -> runner.run(applicationArguments))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Admin bootstrap enabled, but login, password or papelNome is blank");
    }

    @Test
    void runThrowsWhenRoleDoesNotExist() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLogin()).thenReturn("admin");
        when(properties.getPassword()).thenReturn("123");
        when(properties.getPapelNome()).thenReturn("COORDENADOR_ACADEMICO");
        when(papelRepository.findByNome("COORDENADOR_ACADEMICO")).thenReturn(null);

        assertThatThrownBy(() -> runner.run(applicationArguments))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Role not found for admin bootstrap: COORDENADOR_ACADEMICO");
    }

    @Test
    void runCreatesAdminWhenUserDoesNotExist() {
        Papel papel = new Papel();
        papel.setNome("COORDENADOR_ACADEMICO");

        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLogin()).thenReturn("admin");
        when(properties.getPassword()).thenReturn("123");
        when(properties.getPapelNome()).thenReturn("COORDENADOR_ACADEMICO");
        when(papelRepository.findByNome("COORDENADOR_ACADEMICO")).thenReturn(papel);
        when(usuarioRepository.findByLogin("admin")).thenReturn(null);
        when(passwordEncoder.encode("123")).thenReturn("encoded-123");

        runner.run(applicationArguments);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario savedUser = usuarioCaptor.getValue();
        assertThat(savedUser.getLogin()).isEqualTo("admin");
        assertThat(savedUser.getSenha()).isEqualTo("encoded-123");
        assertThat(savedUser.getPapel()).isSameAs(papel);
    }

    @Test
    void runUpdatesUserWhenRoleOrPasswordChanges() {
        Papel papel = new Papel();
        papel.setNome("COORDENADOR_ACADEMICO");
        Usuario usuario = new Usuario();
        usuario.setLogin("admin");
        usuario.setSenha("old-encoded");
        Papel currentRole = new Papel();
        currentRole.setNome("OUTRA_ROLE");
        usuario.setPapel(currentRole);

        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLogin()).thenReturn("admin");
        when(properties.getPassword()).thenReturn("123");
        when(properties.getPapelNome()).thenReturn("COORDENADOR_ACADEMICO");
        when(papelRepository.findByNome("COORDENADOR_ACADEMICO")).thenReturn(papel);
        when(usuarioRepository.findByLogin("admin")).thenReturn(usuario);
        when(passwordEncoder.matches("123", "old-encoded")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded-123");

        runner.run(applicationArguments);

        verify(usuarioRepository).save(usuario);
        assertThat(usuario.getPapel()).isSameAs(papel);
        assertThat(usuario.getSenha()).isEqualTo("encoded-123");
    }

    @Test
    void runDoesNotPersistWhenUserIsAlreadyAligned() {
        Papel papel = new Papel();
        papel.setNome("COORDENADOR_ACADEMICO");
        Usuario usuario = new Usuario();
        usuario.setLogin("admin");
        usuario.setSenha("encoded-123");
        usuario.setPapel(papel);

        when(properties.isEnabled()).thenReturn(true);
        when(properties.getLogin()).thenReturn("admin");
        when(properties.getPassword()).thenReturn("123");
        when(properties.getPapelNome()).thenReturn("COORDENADOR_ACADEMICO");
        when(papelRepository.findByNome("COORDENADOR_ACADEMICO")).thenReturn(papel);
        when(usuarioRepository.findByLogin("admin")).thenReturn(usuario);
        when(passwordEncoder.matches("123", "encoded-123")).thenReturn(true);

        runner.run(applicationArguments);

        verify(usuarioRepository, never()).save(usuario);
    }
}
