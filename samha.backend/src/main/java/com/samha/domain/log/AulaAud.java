package com.samha.domain.log;

import com.samha.domain.Alocacao;
import com.samha.domain.BaseLogEntity;
import com.samha.domain.Oferta;

import javax.persistence.*;

@Entity
@Table(name = "aula_aud")
public class AulaAud extends BaseLogEntity {
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }
    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "numero", updatable = false)
    private int numero;

    @Column(name = "dia", updatable = false)
    private int dia;

    @Column(name = "turno", updatable = false)
    private int turno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alocacao_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Alocacao alocacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oferta_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Oferta oferta;

    public AuditCompositeKey getPk() {
        return pk;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

    public void setPk(AuditCompositeKey pk) {
        this.pk = pk;
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
}
