package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Professor;
import com.samha.domain.log.ProfessorAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/professor")
public class ProfessorController extends BaseController<Professor, ProfessorAud, Long> {
    public ProfessorController(UseCaseFacade facade) {
        super(Professor.class, ProfessorAud.class, facade);
    }
}
