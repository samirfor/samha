package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Papel;
import com.samha.domain.log.PapelAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/papel")
public class PapelController extends BaseController<Papel, PapelAud, Long> {
    public PapelController(UseCaseFacade facade) {
        super(Papel.class, PapelAud.class, facade);
    }
}
