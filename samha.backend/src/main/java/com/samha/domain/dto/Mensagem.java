package com.samha.domain.dto;

import com.samha.domain.Aula;
import com.samha.domain.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class Mensagem {
    private List<String> restricoes = new ArrayList<>();
    private String cor;
    private String titulo;

    private String verificarMensagem;
    //1 vermelho, 2 amarelo, 3 azul.
    private int tipo;

    private List<Aula> aulas = new ArrayList<>();

    private Disciplina disciplina;


    public List<String> getRestricoes() {
        return restricoes;
    }

    public void setRestricoes(List<String> restricoes) {
        this.restricoes = restricoes;
        this.verificarMensagem = "";
        for (var r : restricoes) verificarMensagem += r;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(List<Aula> aulas) {
        this.aulas = aulas;
    }

    public String getVerificarMensagem() {
        return verificarMensagem;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
}
