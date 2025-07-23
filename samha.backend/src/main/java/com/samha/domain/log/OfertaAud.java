package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Turma;

import javax.persistence.*;

@Entity
@Table(name = "oferta_aud")
public class OfertaAud extends BaseLogEntity {
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }
    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "ano", updatable = false)
    private int ano;

    @Column(name = "semestre", updatable = false)
    private int semestre;

    @Column(name = "publica", updatable = false)
    private Boolean publica;

    @Column(name = "tempo_maximo_trabalho", updatable = false)
    private int tempoMaximoTrabalho;

    @Column(name = "intervalo_minimo", updatable = false)
    private int intervaloMinimo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turma_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Turma turma;

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

    public AuditCompositeKey getPk() {
        return pk;
    }

    public void setPk(AuditCompositeKey pk) {
        this.pk = pk;
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

    public int getTempoMaximoTrabalho() {
        return tempoMaximoTrabalho;
    }

    public void setTempoMaximoTrabalho(int tempoMaximoTrabalho) {
        this.tempoMaximoTrabalho = tempoMaximoTrabalho;
    }

    public int getIntervaloMinimo() {
        return intervaloMinimo;
    }

    public void setIntervaloMinimo(int intervaloMinimo) {
        this.intervaloMinimo = intervaloMinimo;
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
}
