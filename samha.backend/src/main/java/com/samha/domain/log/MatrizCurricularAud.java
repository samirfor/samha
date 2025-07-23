package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Curso;

import javax.persistence.*;

@Entity
@Table(name = "matriz_curricular_aud")
public class MatrizCurricularAud extends BaseLogEntity {
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

    @Column(name = "ano", updatable = false)
    private int ano;

    @Column(name = "semestre", updatable = false)
    private int semestre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Curso curso;

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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
