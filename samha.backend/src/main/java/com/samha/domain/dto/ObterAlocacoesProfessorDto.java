package com.samha.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ObterAlocacoesProfessorDto implements Serializable {
    private Long profId;
    private Integer ano;
    private Integer semestre;
}
