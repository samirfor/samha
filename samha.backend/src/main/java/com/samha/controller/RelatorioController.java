package com.samha.controller;

import com.samha.application.disciplina.GerarRelatorioDisciplina;
import com.samha.application.professor.GerarRelatorioProfessor;
import com.samha.application.professor.ObterAulasProfessores;
import com.samha.application.turma.GerarRelatorioTurmas;
import com.samha.application.turma.ObterAulasTurmaRelatorio;
import com.samha.commons.UseCaseFacade;
import com.samha.domain.Professor;
import com.samha.domain.Turma;
import com.samha.domain.dto.RelatorioDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/relatorio")
public class RelatorioController {

    private final UseCaseFacade facade;

    public RelatorioController(UseCaseFacade facade) {
        this.facade = facade;
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
}
