package com.samha.domain;

import com.samha.domain.log.RestricaoProfessorAud;
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
@Table(name = "restricao_professor")
public class RestricaoProfessor extends BaseLogEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column()
    private String descricao;
    
    @Column(nullable = false)
    private int dia;
    
    @Column(nullable = false)
    private String turno;
    
    @Column(nullable = false)
    private boolean aula1;
    
    @Column(nullable = false)
    private boolean aula2;
    
    @Column(nullable = false)
    private boolean aula3;
    
    @Column(nullable = false)
    private boolean aula4;
    
    @Column(nullable = false)
    private boolean aula5;
    
    @Column(nullable = false)
    private boolean aula6;
    
    @Column(nullable = false)
    private String prioridade;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
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
