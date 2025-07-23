package com.samha.application.aula;

import com.samha.commons.CorEnum;
import com.samha.commons.UseCase;
import com.samha.domain.Alocacao;
import com.samha.domain.Alocacao_;
import com.samha.domain.Aula;
import com.samha.domain.Curso_;
import com.samha.domain.Disciplina;
import com.samha.domain.Disciplina_;
import com.samha.domain.MatrizCurricular_;
import com.samha.domain.Professor;
import com.samha.domain.dto.Conflito;
import com.samha.domain.dto.ConflitoDisciplinaRequestDto;
import com.samha.domain.dto.Mensagem;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ObterConflitosQtdDisciplina extends UseCase<Conflito> {


    private Conflito conflito = new Conflito();

    private ConflitoDisciplinaRequestDto request;

    @Inject
    public ObterConflitosQtdDisciplina(ConflitoDisciplinaRequestDto request) {
        this.request = request;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected Conflito execute() throws Exception {
        List<Alocacao> alocacoes = genericRepository.find(Alocacao.class, q -> q.where(
                q.equal(q.get(Alocacao_.ano), request.getAno()),
                q.equal(q.get(Alocacao_.semestre), request.getSemestre()),
                q.equal(q.get(Alocacao_.disciplina).get(Disciplina_.periodo), request.getPeriodo()),
                q.equal(q.get(Alocacao_.disciplina).get(Disciplina_.matriz).get(MatrizCurricular_.curso).get(Curso_.id), request.getCursoId())
        ));

        Set<Disciplina> disciplinas = alocacoes.stream().map(a -> a.getDisciplina()).collect(Collectors.toSet());


        for (var disciplina : disciplinas) {
            List<Alocacao> alocacoesDisciplina = alocacoes.stream().filter(a -> a.getDisciplina().equals(disciplina)).collect(Collectors.toList());
            List<Aula> aulasDisciplina = new ArrayList<>();
            alocacoesDisciplina.forEach(a -> aulasDisciplina.addAll(a.getAulas()));
            List<Aula> aulasRequest = request.getAulasCriadas().stream().filter(a -> a.getAlocacao().getDisciplina().getId().equals(disciplina.getId())).collect(Collectors.toList());
            List<Aula> aulasExcluidas = new ArrayList<>();
            aulasDisciplina.forEach(aulaDisciplina -> {
                Optional<Aula> aulaExcluida = aulasRequest.stream().filter(a -> a.getId() != null).filter(a -> a.getId().equals(aulaDisciplina.getId())).findFirst();
                //se ela não foi excluida
                if (aulaExcluida.isEmpty()) aulasExcluidas.add(aulaDisciplina);
            });
            //filtra aulas excluidas
            aulasExcluidas.forEach(a -> aulasDisciplina.remove(a));
            //adiciona aulas criadas
            aulasDisciplina.addAll(aulasRequest.stream().filter(a -> a.getId() == null).collect(Collectors.toList()));
            if (disciplina.getQtAulas() != aulasDisciplina.size()) {
                Mensagem mensagem = new Mensagem();
                mensagem.setTipo(2);
                mensagem.setCor(CorEnum.LARANJA.getId());
                mensagem.setTitulo("Disciplina: " + disciplina.getSigla());
                mensagem.setDisciplina(disciplina);
                List<String> restrições = new ArrayList<>();
                restrições.add("Aulas alocadas: " + aulasDisciplina.size());
                restrições.add("Quantidade especificada: " + disciplina.getQtAulas());
                mensagem.setRestricoes(restrições);
                conflito.getMensagens().add(mensagem);
                //Feito dessa forma, pois, este tipo de notificação não possui professor atrelado.
                //Para não mudar toda a lógica de processamento dos conflitos, foi criado um professor fake para aparecer como título do card.
                Professor fake = new Professor();
                fake.setNome("Quantidade de aulas diferente da especificada");
                conflito.setProfessor(fake);
            }
        }




        return conflito;
    }
}
