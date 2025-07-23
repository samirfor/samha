package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Professor;

import javax.persistence.*;

@Entity
@Table(name = "restricao_professor_aud")
public class RestricaoProfessorAud extends BaseLogEntity {

    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "nome", updatable = false)
    private String nome;

    @Column(name = "descricao", updatable = false)
    private String descricao;

    @Column(name = "dia", updatable = false)
    private Integer dia;

    @Column(name = "turno", updatable = false)
    private String turno;

    @Column(name = "aula1", updatable = false)
    private boolean aula1;

    @Column(name = "aula2", updatable = false)
    private boolean aula2;

    @Column(name = "aula3", updatable = false)
    private boolean aula3;

    @Column(name = "aula4", updatable = false)
    private boolean aula4;

    @Column(name = "aula5", updatable = false)
    private boolean aula5;

    @Column(name = "aula6", updatable = false)
    private boolean aula6;

    @Column(name = "prioridade", updatable = false)
    private String prioridade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Professor professor;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public boolean isAula1() {
        return aula1;
    }

    public void setAula1(boolean aula1) {
        this.aula1 = aula1;
    }

    public boolean isAula2() {
        return aula2;
    }

    public void setAula2(boolean aula2) {
        this.aula2 = aula2;
    }

    public boolean isAula3() {
        return aula3;
    }

    public void setAula3(boolean aula3) {
        this.aula3 = aula3;
    }

    public boolean isAula4() {
        return aula4;
    }

    public void setAula4(boolean aula4) {
        this.aula4 = aula4;
    }

    public boolean isAula5() {
        return aula5;
    }

    public void setAula5(boolean aula5) {
        this.aula5 = aula5;
    }

    public boolean isAula6() {
        return aula6;
    }

    public void setAula6(boolean aula6) {
        this.aula6 = aula6;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public Class getLogEntity() {
        return RestricaoProfessorAud.class;
    }
}
