package com.samha.application.alocacao;

import com.samha.commons.UseCase;
import com.samha.domain.Alocacao;
import com.samha.domain.Alocacao_;
import com.samha.domain.Aula;
import com.samha.domain.Professor_;
import com.samha.domain.Turma;
import com.samha.domain.dto.ObterAlocacoesProfessorDto;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ObterAlocacoesProfessor extends UseCase<List<Alocacao>> {

    private ObterAlocacoesProfessorDto obterAlocacoesProfessorDto;

    @Inject
    public ObterAlocacoesProfessor(ObterAlocacoesProfessorDto obterAlocacoesProfessorDto) {
        this.obterAlocacoesProfessorDto = obterAlocacoesProfessorDto;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected List<Alocacao> execute() throws Exception {
        List<Alocacao> alocacoes = genericRepository.find(Alocacao.class, q -> q.where(
                q.equal(q.get(Alocacao_.ano), obterAlocacoesProfessorDto.getAno()),
                q.equal(q.get(Alocacao_.semestre), obterAlocacoesProfessorDto.getSemestre()),
                q.or(
                        q.equal(q.get(Alocacao_.professor1).get(Professor_.id), obterAlocacoesProfessorDto.getProfId()),
                        q.equal(q.get(Alocacao_.professor2).get(Professor_.id), obterAlocacoesProfessorDto.getProfId())
                )
        ));
        List<Alocacao> alocacoesProcessadas = new ArrayList<>();

        for (var alocacao : alocacoes) {
            Set<Turma> turmas = alocacao.getAulas().stream().map(a -> a.getOferta().getTurma()).collect(Collectors.toSet());
            for (var turma : turmas) {
                Alocacao alocacaoProcessada = new Alocacao();
                List<Aula> aulasDaTurma = alocacao.getAulas().stream().filter(a -> a.getOferta().getTurma().equals(turma)).collect(Collectors.toList());
                alocacaoProcessada.setCompleta(alocacao.getDisciplina().getQtAulas() == aulasDaTurma.size());
                alocacaoProcessada.setTurma(turma);
                alocacaoProcessada.setDisciplina(alocacao.getDisciplina());
                alocacoesProcessadas.add(alocacaoProcessada);
            }
        }
        return alocacoesProcessadas;
    }
}
