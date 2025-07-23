package com.samha.application.coordenador;

import com.samha.application.commons.QueryEntities;
import com.samha.domain.Professor;
import com.samha.persistence.filter.PagedList;
import com.samha.persistence.filter.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultarCoordenadores extends QueryEntities<Professor> {

    public ConsultarCoordenadores(Query query) {
        super(query, Professor.class);
    }

    @Override
    protected PagedList execute() throws Exception {
        this.buildCoordenadorPredicate();
        return super.execute();
    }

    private void buildCoordenadorPredicate() {
        Map<String, Object> papelNomeEqual = new HashMap<>();
        papelNomeEqual.put("equals", "COORDENADOR_CURSO");

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("usuario.papel.nome", papelNomeEqual);

        this.entityQuery.getPredicates().put("and", usuarioMap);
    }
}
