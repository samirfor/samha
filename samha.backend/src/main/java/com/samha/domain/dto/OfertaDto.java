package com.samha.domain.dto;

import com.samha.domain.Aula;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OfertaDto implements Serializable {
    private List<Aula> aulas;
    private Long ofertaId;
    public List<Aula> getAulas() {
        return aulas;
    }

}
