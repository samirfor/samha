package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Coordenadoria;
import com.samha.domain.log.CoordenadoriaAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/coordenadoria")
public class CoordenadoriaController extends BaseController<Coordenadoria, CoordenadoriaAud, Long> {
    public CoordenadoriaController(UseCaseFacade facade) {
        super(Coordenadoria.class, CoordenadoriaAud.class, facade);
    }
}
