package com.samha.domain;

import com.samha.domain.log.DisciplinaAud;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "disciplina")
public class Disciplina extends BaseLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String sigla;
    
    @Column(nullable = false)
    private String tipo;
    
    @Column(nullable = false)
    private double cargaHoraria;
    
    @Column(nullable = false)
    private int qtAulas;
    
    @Column(nullable = false)
    private int periodo;

    @Column(nullable = false)
    private int qtVagas;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_id", nullable = false)
    private MatrizCurricular matriz;

    public Disciplina() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public int getQtAulas() {
        return qtAulas;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public void setQtAulas(int qtAulas) {
        this.qtAulas = qtAulas;
    }

    public MatrizCurricular getMatriz() {
        return matriz;
    }

    public void setMatriz(MatrizCurricular matriz) {
        this.matriz = matriz;
    }

    public int getQtVagas(){
        return qtVagas;
    }

    public void setQtVagas(int qtVagas){
        this.qtVagas = qtVagas;
    }

    @Override
    public String toString() {
        return nome;
    }

    public double getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public Class getLogEntity() {
        return DisciplinaAud.class;
    }
}
