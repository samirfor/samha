package com.samha.application.turma;

import com.samha.commons.UseCase;
import com.samha.domain.Turma;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ObterPeriodoAtualTurma extends UseCase<Integer> {

    private Long id;

    @Inject
    public ObterPeriodoAtualTurma(Long id) {
        this.id = id;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected Integer execute() throws Exception {
        Turma turma = this.genericRepository.get(Turma.class, id);
        return getPeriodoAtual(turma);
    }

    public static Integer getPeriodoAtual(Turma turma) {
        Integer anoAtual = LocalDateTime.now().getYear();
        if(!turma.getMatriz().getCurso().getSemestral()) {
            int qtAnos = anoAtual - turma.getAno() + 1;
            if (qtAnos > turma.getMatriz().getCurso().getQtPeriodos()) return turma.getMatriz().getCurso().getQtPeriodos();
            return qtAnos;
        } else {
            int qtAnos = (anoAtual - turma.getAno()) * 2;
            int semestreAtual;
            if(Calendar.getInstance().get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.JULY)){
                semestreAtual = 2;
            }else{
                semestreAtual = 1;
            }

            if(semestreAtual == turma.getSemestre()) qtAnos += 1;
            if(semestreAtual == 2 && turma.getSemestre() == 1) qtAnos += 2;
            if (qtAnos > turma.getMatriz().getCurso().getQtPeriodos()) return turma.getMatriz().getCurso().getQtPeriodos();
            return qtAnos;
        }
    }
}
