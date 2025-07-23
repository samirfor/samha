package com.samha.domain.dto;

import com.samha.domain.Aula;

import java.io.Serializable;
import java.util.List;

public class ConflitoDisciplinaRequestDto implements Serializable {
    private Integer ano;
    private Integer semestre;
    private Long cursoId;
    private Integer periodo;

    private List<Aula> aulasCriadas;

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public List<Aula> getAulasCriadas() {
        return aulasCriadas;
    }

    public void setAulasCriadas(List<Aula> aulasCriadas) {
        this.aulasCriadas = aulasCriadas;
    }
}
