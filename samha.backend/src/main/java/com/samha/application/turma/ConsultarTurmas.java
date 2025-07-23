package com.samha.application.turma;


import com.samha.application.commons.QueryEntities;
import com.samha.domain.Turma;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;


import java.util.LinkedHashMap;
import java.util.List;

public class ConsultarTurmas extends QueryEntities<Turma> {


    public ConsultarTurmas(Query query) {
        super(query, Turma.class);
    }

    @Override
    protected PagedList execute() throws Exception {
        this.tratarEntityQuery();


        return super.execute();
    }

    private void tratarEntityQuery() {
        LinkedHashMap<String, Object> and = (LinkedHashMap<String, Object>) this.entityQuery.getPredicates().get("and");

        if(and != null && !and.isEmpty()) {
            Boolean ativas = (Boolean) and.get("ativas");
            if (ativas) {
                String anoFiltro = (String) and.get("ano");
                String semestreFiltro = (String) and.get("semestre");

                try {
                    Integer anoFiltroInt = Integer.parseInt(anoFiltro);
                    Integer semestreFiltroInt = Integer.parseInt(semestreFiltro);

                    List<Turma> turmas = this.genericRepository.find(Turma.class, q -> q.where(

                    ));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }


            and.remove("ativas");
            and.remove("ano");
            and.remove("semestre");
        }
    }


}
