package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.RestricaoProfessor;
import com.samha.domain.log.RestricaoProfessorAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/restricaoProfessor")
public class RestricaoProfessorController extends BaseController<RestricaoProfessor, RestricaoProfessorAud, Long> {
    public RestricaoProfessorController(UseCaseFacade facade) {
        super(RestricaoProfessor.class, RestricaoProfessorAud.class, facade);
    }
}
