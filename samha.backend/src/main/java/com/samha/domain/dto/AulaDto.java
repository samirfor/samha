package com.samha.domain.dto;

public class AulaDto {
    private Long id;
    private int dia;
    private int numero;

    private String nomeTurma;
    private String siglaDisciplina;

    private String nomeDisciplina;

    public AulaDto(Long id, int dia, int numero, String nomeTurma, String siglaDisciplina, String nomeDisciplina) {
        this.id = id;
        this.dia = dia;
        this.numero = numero;
        this.nomeTurma = nomeTurma;
        this.siglaDisciplina = siglaDisciplina;
        this.nomeDisciplina = nomeDisciplina;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getSiglaDisciplina() {
        return siglaDisciplina;
    }

    public void setSiglaDisciplina(String siglaDisciplina) {
        this.siglaDisciplina = siglaDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
}
