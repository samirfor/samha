package com.samha.controller;

import com.samha.application.oferta.InserirOferta;
import com.samha.application.oferta.MudarVisibilidadeOferta;
import com.samha.application.oferta.PermissaoMudarVisibilidade;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Oferta;
import com.samha.domain.log.OfertaAud;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/oferta")
public class OfertaController extends BaseController<Oferta, OfertaAud, Long> {
    public OfertaController(UseCaseFacade facade) {
        super(Oferta.class, OfertaAud.class, facade);
    }

    @PostMapping("mudar-visiblidade")
    public Oferta tornarOfertaPublica(@RequestBody Long ofertaId) {
     return facade.execute(new MudarVisibilidadeOferta(ofertaId));
    }

    @PostMapping("permissaoMudarVisibilidade")
    public Boolean permissaoMudarVisibilidade(HttpServletRequest request) {
        return facade.execute(new PermissaoMudarVisibilidade(request));
    }


    @Override
    public Oferta insert(@RequestBody Oferta body) {
        return facade.execute(new InserirOferta(body));
    }


}
