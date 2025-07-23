package com.samha.controller;

import com.samha.application.commons.QueryEntities;
import com.samha.application.disciplina.GerarRelatorioDisciplina;
import com.samha.application.professor.GerarRelatorioProfessor;
import com.samha.application.professor.ObterAulasProfessores;
import com.samha.application.turma.GerarRelatorioTurmas;
import com.samha.application.turma.ObterAulasTurmaRelatorio;
import com.samha.commons.BusinessException;
import com.samha.commons.UseCaseFacade;
import com.samha.domain.Coordenadoria;
import com.samha.domain.Curso;
import com.samha.domain.Eixo;
import com.samha.domain.Label;
import com.samha.domain.Professor;
import com.samha.domain.Turma;
import com.samha.domain.dto.RelatorioDto;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import com.samha.persistence.generics.IGenericRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("api/public")
@RestController
public class PublicController {

    private final UseCaseFacade facade;
    private final IGenericRepository genericRepository;

    public PublicController(UseCaseFacade facade, IGenericRepository genericRepository) {
        this.facade = facade;
        this.genericRepository = genericRepository;
    }

    @GetMapping("eixo/all")
    public List<Eixo> getEixo() {
        return genericRepository.findAll(Eixo.class);
    }

    @GetMapping("label/all")
    public List<Label> getLabel() {return genericRepository.findAll(Label.class);}

    @PostMapping("coordenadoria/query")
    public PagedList getCoordenadorias(@RequestBody Query query) {
        this.validateQuery(query);
        return facade.execute(new QueryEntities<>(query, Coordenadoria.class));
    }

    @PostMapping("professor/query")
    public PagedList getProfessoers(@RequestBody Query query) {
        this.validateQuery(query);
        return facade.execute(new QueryEntities<>(query, Professor.class));
    }

    @PostMapping("curso/query")
    public PagedList getCursos(@RequestBody Query query) {
        this.validateQuery(query);
        return facade.execute(new QueryEntities<>(query, Curso.class));
    }

    @PostMapping("turma/query")
    public PagedList getTurmas(@RequestBody Query query) {
        this.validateQuery(query);
        return facade.execute(new QueryEntities<>(query, Turma.class));
    }

    @PostMapping("gerar-relatorio-professor")
    public Map<String, Object> gerarRelatorioProfessor(@RequestBody RelatorioDto relatorioDto) {
        return facade.execute(new GerarRelatorioProfessor(relatorioDto));
    }
    @PostMapping("obter-aulas-turma")
    public List<Turma> obterAulasTurma(@RequestBody RelatorioDto relatorioDto, HttpServletRequest request) {
        return facade.execute(new ObterAulasTurmaRelatorio(relatorioDto));
    }

    @PostMapping("gerar-relatorio-turma")
    public Map<String, Object> gerarRelatorioTurma(@RequestBody RelatorioDto relatorioDto) {
        return facade.execute(new GerarRelatorioTurmas(relatorioDto));
    }

    @PostMapping("obter-professores-relatorio")
    public List<Professor> obterProfessoresRelatorio(@RequestBody RelatorioDto relatorioDto) {
        return facade.execute(new ObterAulasProfessores(relatorioDto));
    }

    @PostMapping("gerar-relatorio-disciplina")
    public Map<String, Object> gerarRelatorioDisciplina(@RequestBody RelatorioDto relatorioDto) {
        return facade.execute(new GerarRelatorioDisciplina(relatorioDto));
    }

    private void validateQuery(Query query) {
        query.getProjections().forEach( p -> {
            if (!(p.equals("id") || p.equals("nome"))) throw new BusinessException("Você está tentando utilizar parâmetros não permitidos.");
        });
    }
}
