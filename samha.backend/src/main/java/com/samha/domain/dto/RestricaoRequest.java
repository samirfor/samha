package com.samha.domain.dto;

import com.samha.domain.Aula;
import com.samha.domain.Oferta;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RestricaoRequest implements Serializable {
    private List<Aula> aulas;
    private Oferta oferta;
}
