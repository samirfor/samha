package com.samha.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RelatorioDisciplinaOfertadaDto implements Serializable {
    private Long cursoId;
    private Integer ano;
    private Integer semestre;
    private String nomeCurso;
}
