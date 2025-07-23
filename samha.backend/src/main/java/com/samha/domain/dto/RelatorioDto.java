package com.samha.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RelatorioDto implements Serializable {
    private Integer ano;
    private Integer semestre;
    private Long eixoId;
    private Long professorId;
    private Long coordenadoriaId;
    private Boolean enviarEmail;
    private String senha;
    private Long turmaId;
    private Long cursoId;
    private String nomeRelatorio;
}
