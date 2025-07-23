package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.MatrizCurricular;

import javax.persistence.*;

@Entity
@Table(name = "turma_aud")
public class TurmaAud extends BaseLogEntity {
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }

    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "nome", updatable = false)
    private String nome;

    @Column(name = "turno", updatable = false)
    private String turno;

    @Column(name = "ano", updatable = false)
    private int ano;

    @Column(name = "semestre", updatable = false)
    private int semestre;

    @Column(name = "ativa", updatable = false)
    private Boolean ativa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_curricular_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MatrizCurricular matriz;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
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

    public MatrizCurricular getMatriz() {
        return matriz;
    }

    public void setMatriz(MatrizCurricular matriz) {
        this.matriz = matriz;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
