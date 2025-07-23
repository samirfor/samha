package com.samha.controller;

import com.samha.application.alocacao.ConsultarAlocacoes;
import com.samha.application.alocacao.ObterAlocacoesProfessor;
import com.samha.application.alocacao.ObterCargaHoraria;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Alocacao;
import com.samha.domain.Professor;
import com.samha.domain.dto.ObterAlocacoesProfessorDto;
import com.samha.domain.log.AlocacaoAud;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/alocacao")
public class AlocacaoController extends BaseController<Alocacao, AlocacaoAud, Long> {
    public AlocacaoController(UseCaseFacade facade) {
        super(Alocacao.class, AlocacaoAud.class, facade);
    }
    
    @PostMapping("obter-carga-horaria")
    public List<Professor> obterCargaHoraria(@RequestBody HashMap<String, String> params) {
        return facade.execute(new ObterCargaHoraria(params));
    }

    @PostMapping("obter-alocacoes-professor")
    public List<Alocacao> obterAlocacoesProfessor(@RequestBody ObterAlocacoesProfessorDto obterAlocacoesProfessorDto) {
        return facade.execute(new ObterAlocacoesProfessor(obterAlocacoesProfessorDto));
    }

    @Override
    public PagedList buildQueryEntities(Query query) {
        return facade.execute(new ConsultarAlocacoes(query));
    }
}
