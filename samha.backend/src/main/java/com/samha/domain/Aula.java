package com.samha.domain;

import com.samha.domain.log.AulaAud;
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
import javax.persistence.Transient;

@Entity
@Audited
@Table(name = "aula")
public class Aula extends BaseLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private int numero;
    
    @Column(nullable = false)
    private int dia;
    
    @Column(nullable = false)
    private int turno;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alocacao_id")
    private Alocacao alocacao;
     
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oferta_id", nullable = false)
    private Oferta oferta;

    @Transient
    private Boolean completa;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public Alocacao getAlocacao() {
        return alocacao;
    }

    public void setAlocacao(Alocacao alocacao) {
        this.alocacao = alocacao;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public Boolean getCompleta() {
        return completa;
    }

    public void setCompleta(Boolean completa) {
        this.completa = completa;
    }

    @Override
    public Class getLogEntity() {
        return AulaAud.class;
    }
}
