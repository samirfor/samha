package com.samha.controller;

import com.samha.application.aula.ObterConflitosQtdDisciplina;
import com.samha.application.aula.ObterConflitosTurmas;
import com.samha.application.aula.ObterRestricoesAulas;
import com.samha.application.aula.SalvarAulas;
import com.samha.application.aula.CopiarAulas;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Aula;
import com.samha.domain.dto.Conflito;
import com.samha.domain.dto.ConflitoDisciplinaRequestDto;
import com.samha.domain.dto.ConflitoTurma;
import com.samha.domain.dto.OfertaDto;
import com.samha.domain.dto.RestricaoRequest;
import com.samha.domain.log.AulaAud;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/aula")
public class AulaController extends BaseController<Aula, AulaAud, Long> {
    public AulaController(UseCaseFacade facade) {
        super(Aula.class, AulaAud.class, facade);
    }

    @PostMapping("obter-restricoes")
    public List<Conflito> obterRestricoes(@RequestBody RestricaoRequest restricaoRequest) {
        return facade.execute(new ObterRestricoesAulas(restricaoRequest));
    }

    @PostMapping("controle-qtd-disciplina")
    public Conflito obterConflitosQtdDisciplina(@RequestBody ConflitoDisciplinaRequestDto request) {
        return facade.execute(new ObterConflitosQtdDisciplina(request));
    }

    @PostMapping("salvar-aulas")
    public List<Aula> salvarAulas(@RequestBody OfertaDto ofertaDto) {
        return facade.execute(new SalvarAulas(ofertaDto));
    }

    @PostMapping("validar-turmas/{ano}/{semestre}")
    public List<ConflitoTurma> getValidacaoTurmas(@PathVariable Integer ano, @PathVariable Integer semestre) {
        return facade.execute(new ObterConflitosTurmas(ano, semestre));
    }

    @PostMapping("copiar-aulas/{turmaId}/{ano}/{ofertaIdDestino}")
    public void copiarAulas(@PathVariable Integer turmaId, @PathVariable Integer ano, @PathVariable Integer ofertaIdDestino) {
        facade.execute(new CopiarAulas(turmaId, ano, ofertaIdDestino));
    }
}
