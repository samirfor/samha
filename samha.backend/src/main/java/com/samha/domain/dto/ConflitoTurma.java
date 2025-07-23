package com.samha.domain.dto;

import com.samha.domain.Turma;

import java.util.List;

public class ConflitoTurma {
    private List<Conflito> conflitos;
    private Turma turma;

    public List<Conflito> getConflitos() {
        return conflitos;
    }

    public void setConflitos(List<Conflito> conflitos) {
        this.conflitos = conflitos;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
}
