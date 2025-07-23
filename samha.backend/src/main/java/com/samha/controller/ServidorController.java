package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Servidor;
import com.samha.domain.log.ServidorAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/servidor")
public class ServidorController extends BaseController<Servidor, ServidorAud, Long> {
    public ServidorController(UseCaseFacade facade) {
        super(Servidor.class, ServidorAud.class, facade);
    }
}
