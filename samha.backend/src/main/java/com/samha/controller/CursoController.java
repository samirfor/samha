package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Curso;
import com.samha.domain.log.CursoAud;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/curso")
public class CursoController extends BaseController<Curso, CursoAud, Long> {
    public CursoController(UseCaseFacade facade) {
        super(Curso.class, CursoAud.class, facade);
    }
}
