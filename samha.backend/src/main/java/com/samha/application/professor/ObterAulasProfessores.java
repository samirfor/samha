package com.samha.application.professor;

import com.samha.commons.UseCase;
import com.samha.domain.Alocacao_;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.domain.Coordenadoria_;
import com.samha.domain.Eixo_;
import com.samha.domain.Oferta_;
import com.samha.domain.Professor;
import com.samha.domain.Professor_;
import com.samha.domain.Turma_;
import com.samha.domain.dto.AulaDto;
import com.samha.domain.dto.RelatorioDto;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.persistence.generics.IQueryHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ObterAulasProfessores extends UseCase<List<Professor>> {

    private RelatorioDto relatorioDto;

    @Inject
    public ObterAulasProfessores(RelatorioDto relatorioDto) {
        this.relatorioDto = relatorioDto;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected List<Professor> execute() throws Exception {
        if(relatorioDto.getProfessorId() != null){
            List<Professor> professores = genericRepository.find(Professor.class, q -> q.where(
                    q.equal(q.get(Professor_.id), relatorioDto.getProfessorId()),
                    q.equal(q.get(Professor_.ativo), true)
            )).stream().sorted(Comparator.comparing(Professor::getNome)).collect(Collectors.toList());
            this.setAulasProfessores(professores);
            return professores;
        }
        else if(relatorioDto.getCoordenadoriaId() != null) {
            List<Professor> professores = genericRepository.find(Professor.class, q -> q.where(
                    q.equal(q.get(Professor_.coordenadoria).get(Coordenadoria_.id), relatorioDto.getCoordenadoriaId()),
                    q.equal(q.get(Professor_.ativo), true)
            )).stream().sorted(Comparator.comparing(Professor::getNome)).collect(Collectors.toList());;
            this.setAulasProfessores(professores);
            return professores;
        }
        else if(relatorioDto.getEixoId() != null) {
            List<Professor> professors = genericRepository.find(Professor.class, q -> q.where(
                    q.equal(q.get(Professor_.coordenadoria).get(Coordenadoria_.eixo).get(Eixo_.id), relatorioDto.getEixoId()),
                    q.equal(q.get(Professor_.ativo), true)
            )).stream().sorted(Comparator.comparing(Professor::getNome)).collect(Collectors.toList());;
            this.setAulasProfessores(professors);
            return professors;
        }
        else {
            List<Professor> professors = genericRepository.find(Professor.class, q -> q.where(
                    q.equal(q.get(Professor_.ativo), true)
            )).stream().sorted(Comparator.comparing(Professor::getNome)).collect(Collectors.toList());;
            this.setAulasProfessores(professors);
            return professors;
        }
    }

    private void setAulasProfessores(List<Professor> professores) {

        for (var prof : professores) {
            List<Aula> aulas = genericRepository.find(Aula.class, q -> q.where(
                    q.or(
                            q.equal(q.get(Aula_.alocacao).get(Alocacao_.professor1), prof),
                            q.equal(q.get(Aula_.alocacao).get(Alocacao_.professor2), prof)
                    ),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.ano), relatorioDto.getAno()),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.turma).get(Turma_.ativa), true),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.semestre), relatorioDto.getSemestre()),
                    getFiltroOfertaPublicaPredicate(q)
            ));

            List<AulaDto> aulasDto = new ArrayList<>();
            for (var a : aulas) {
                AulaDto aulaDto = new AulaDto(a.getId(), a.getDia(), a.getNumero(), a.getOferta().getTurma().getNome(), a.getAlocacao().getDisciplina().getSigla(), a.getAlocacao().getDisciplina().getNome());
                aulasDto.add(aulaDto);
            }
            prof.setAulas(aulasDto);
        }
    }

    private Predicate getFiltroOfertaPublicaPredicate(IQueryHelper<Aula, Aula> q) {
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken;
        if (isAuthenticated) {
            return q.or(
                    q.equal(q.get(Aula_.oferta).get(Oferta_.publica), true),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.publica), false)
            );
        } else {
            return q.equal(q.get(Aula_.oferta).get(Oferta_.publica), true);
        }
    }

    public IGenericRepository getGenericRepository() {
        return genericRepository;
    }

    public void setGenericRepository(IGenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
