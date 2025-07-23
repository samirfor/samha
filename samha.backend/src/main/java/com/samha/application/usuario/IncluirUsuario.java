package com.samha.application.usuario;


import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.Papel;
import com.samha.domain.Servidor;
import com.samha.domain.Servidor_;
import com.samha.domain.Usuario;
import com.samha.domain.dto.UsuarioDto;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.service.IUsuarioService;

import javax.inject.Inject;

public class IncluirUsuario extends UseCase<Usuario> {

    private UsuarioDto usuarioDto;

    @Inject
    public IncluirUsuario(UsuarioDto usuario) {
        this.usuarioDto = usuario;
    }

    @Inject
    private IUsuarioService usuarioService;

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected Usuario execute() throws Exception {
        if (usuarioDto.getId() == null) {
            Usuario user = new Usuario();

            validarNovoUsuario();

            user.setLogin(this.usuarioDto.getLogin());
            user.setSenha(this.usuarioDto.getSenha());
            Papel papel = this.genericRepository.get(Papel.class, this.usuarioDto.getPapel_id());
            user.setPapel(papel);
            user = this.usuarioService.saveUsuario(user);

            if(this.usuarioDto.getServidor_id() != null){
                Servidor servidor = this.genericRepository.get(Servidor.class, this.usuarioDto.getServidor_id());
                if (servidor.getUsuario() == null) servidor.setUsuario(user);
                else throw new BusinessException("Este servidor já possui um usuário cadastrado!");
                this.genericRepository.save(servidor);
            }
        } else {
            Usuario usuarioBanco = genericRepository.get(Usuario.class, usuarioDto.getId());
            Servidor servidor = genericRepository.findSingle(Servidor.class, q -> q.where(
                    q.equal(q.get(Servidor_.usuario), usuarioBanco)
            ));

            if(servidor != null && usuarioDto.getServidor_id() == null) {
                servidor.setUsuario(null);
                genericRepository.save(servidor);
            } else if(servidor != null && servidor.getId() != usuarioDto.getServidor_id()) {
                Servidor novoServidor = genericRepository.get(Servidor.class, usuarioDto.getServidor_id());
                if (novoServidor.getUsuario() != null) throw new BusinessException("Este servidor já possui um usuário registrado!");
                else {
                    novoServidor.setUsuario(usuarioBanco);
                    servidor.setUsuario(null);
                    genericRepository.save(servidor);
                    genericRepository.save(novoServidor);
                }
            } else if (usuarioDto.getServidor_id() != null) {
                Servidor associarServidor = genericRepository.get(Servidor.class, usuarioDto.getServidor_id());
                associarServidor.setUsuario(usuarioBanco);
                genericRepository.save(associarServidor);
            }

            usuarioBanco.setLogin(usuarioDto.getLogin());
            usuarioBanco.setPapel(genericRepository.get(Papel.class, usuarioDto.getPapel_id()));
            if (usuarioDto.getSenha() != null) usuarioBanco.setSenha(usuarioDto.getSenha());
            return usuarioService.saveUsuario(usuarioBanco);
        }

        return new Usuario();
    }

    private void validarNovoUsuario() {
        Usuario user = usuarioService.findByLogin(usuarioDto.getLogin());
        if (user != null) throw new BusinessException("Este login já foi utilizado por outro usuário.");
    }
}
