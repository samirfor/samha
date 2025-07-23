package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Disciplina;
import com.samha.domain.Professor;

import javax.persistence.*;

@Entity
@Table(name = "alocacao_aud")
public class AlocacaoAud extends BaseLogEntity {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disciplina_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor1_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor2_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor2;

    public AuditCompositeKey getPk() {
        return pk;
    }

    public void setPk(AuditCompositeKey pk) {
        this.pk = pk;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
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

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor1() {
        return professor1;
    }

    public void setProfessor1(Professor professor1) {
        this.professor1 = professor1;
    }

    public Professor getProfessor2() {
        return professor2;
    }

    public void setProfessor2(Professor professor2) {
        this.professor2 = professor2;
    }
}
