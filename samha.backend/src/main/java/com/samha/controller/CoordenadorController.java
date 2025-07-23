package com.samha.controller;

import com.samha.application.coordenador.ConsultarCoordenadores;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Professor;
import com.samha.domain.log.ProfessorAud;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/coordenador")
public class CoordenadorController extends BaseController<Professor, ProfessorAud, Long> {
    public CoordenadorController(UseCaseFacade facade) {
        super(Professor.class, ProfessorAud.class, facade);
    }

    @Override
    public PagedList buildQueryEntities(Query query) {
        return this.facade.execute(new ConsultarCoordenadores(query));
    }
}
