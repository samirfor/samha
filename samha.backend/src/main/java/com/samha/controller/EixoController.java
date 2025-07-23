package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Eixo;
import com.samha.domain.log.EixoAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/eixo")
public class EixoController extends BaseController<Eixo, EixoAud, Long> {

    public EixoController(UseCaseFacade facade) {
        super(Eixo.class, EixoAud.class, facade);
    }
}
