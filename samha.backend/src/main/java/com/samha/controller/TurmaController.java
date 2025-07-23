package com.samha.controller;


import com.samha.application.turma.AtualizarTurmasAtivas;
import com.samha.application.turma.ObterPeriodoAtualTurma;
import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Turma;
import com.samha.domain.log.TurmaAud;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/turma")
public class TurmaController extends BaseController<Turma, TurmaAud, Long> {
    public TurmaController(UseCaseFacade facade) {
        super(Turma.class, TurmaAud.class, facade);
    }

    @PostMapping("atualizarTurmas")
    public Boolean atualizarTurmasAtivas(){
        return facade.execute(new AtualizarTurmasAtivas());
    }

    @GetMapping("getPeriodoAtual/{id}")
    public Integer getPeriodoAtual(@PathVariable Long id) {
        return facade.execute(new ObterPeriodoAtualTurma(id));
    }
}
