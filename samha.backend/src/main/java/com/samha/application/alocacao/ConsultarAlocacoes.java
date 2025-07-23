package com.samha.application.alocacao;

import com.samha.application.commons.QueryEntities;
import com.samha.domain.Alocacao;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsultarAlocacoes extends QueryEntities<Alocacao> {

    @Inject
    public ConsultarAlocacoes(Query query) {
        super(query, Alocacao.class);
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected PagedList execute() throws Exception {
        PagedList pagedList = super.execute();
        List<Map<String, Object>> map = pagedList.getListMap();
        map.forEach(m -> {
            Alocacao alocacao = genericRepository.get(Alocacao.class, (Long) m.get("id"));
            List<Aula> aulasAlocacao = genericRepository.find(Aula.class, q -> q.where(
                    q.equal(q.get(Aula_.alocacao), alocacao)
            ));
            Integer qtdTurmas = aulasAlocacao.stream().map(a -> a.getOferta().getTurma()).collect(Collectors.toSet()).size();
            if (qtdTurmas < 1) qtdTurmas = 1;
            if(aulasAlocacao.size() == (alocacao.getDisciplina().getQtAulas() * qtdTurmas)){
                m.put("completa", true);
            } else {
                m.put("completa", false);
            }


            m.put("encurtadoProfessor1", alocacao.getProfessor1().obterNomeAbreviado());
            m.put("encurtadoProfessor2", alocacao.getProfessor2() != null ? alocacao.getProfessor2().obterNomeAbreviado() : "");
        });
        return pagedList;
    }
}
