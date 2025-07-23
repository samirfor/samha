package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Coordenadoria;
import com.samha.domain.Professor;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "curso_aud")
public class CursoAud extends BaseLogEntity {
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

    @Column(name = "qt_periodos", updatable = false)
    private Integer qtPeriodos;

    @Column(name = "nivel", updatable = false)
    private String nivel;

    @Column(name = "semestral", updatable = false)
    private Boolean semestral;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordenadoria_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Coordenadoria coordenadoria;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor;

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

    public Integer getQtPeriodos() {
        return qtPeriodos;
    }

    public void setQtPeriodos(Integer qtPeriodos) {
        this.qtPeriodos = qtPeriodos;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Coordenadoria getCoordenadoria() {
        return coordenadoria;
    }

    public void setCoordenadoria(Coordenadoria coordenadoria) {
        this.coordenadoria = coordenadoria;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

    public Boolean getSemestral() {
        return semestral;
    }

    public void setSemestral(Boolean semestral) {
        this.semestral = semestral;
    }
}
