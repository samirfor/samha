package com.samha.domain;

import com.samha.domain.log.OfertaAud;
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
@Table(name = "oferta")
public class Oferta extends BaseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private int ano;
    
    @Column(nullable = false)
    private int semestre;
    
    @Column(nullable = false)
    private int tempoMaximoTrabalho;
    
    @Column(nullable = false)
    private int intervaloMinimo;

    @Column(nullable = false)
    private Boolean publica;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    public double getTempoMaximoTrabalho() {
        return tempoMaximoTrabalho;
    }

    public void setTempoMaximoTrabalho(int tempoMaximoTrabalho) {
        this.tempoMaximoTrabalho = tempoMaximoTrabalho;
    }

    public double getIntervaloMinimo() {
        return intervaloMinimo;
    }

    public void setIntervaloMinimo(int intervaloMinimo) {
        this.intervaloMinimo = intervaloMinimo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Boolean getPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    @Override
    public Class getLogEntity() {
        return OfertaAud.class;
    }
}
