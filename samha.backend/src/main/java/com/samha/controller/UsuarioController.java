package com.samha.controller;


import com.samha.application.usuario.IncluirUsuario;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Usuario;
import com.samha.domain.dto.UsuarioDto;
import com.samha.domain.log.UsuarioAud;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController extends BaseController<Usuario, UsuarioAud, Long> {


    public UsuarioController(UseCaseFacade facade) {
        super(Usuario.class, UsuarioAud.class, facade);
    }

    @PostMapping("/newUser")
    public Usuario insert(@RequestBody UsuarioDto body){
        return this.facade.execute(new IncluirUsuario(body));
    }

}
