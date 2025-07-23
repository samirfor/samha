package com.samha.domain;

import com.samha.domain.dto.AulaDto;
import com.samha.domain.log.TurmaAud;
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
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Table(name = "turma")
public class Turma extends BaseLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nome;
    
    @Column(nullable = false)
    private String turno;
    
    @Column(nullable = false)
    private int ano;
    
    @Column(nullable = false)
    private int semestre;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriz_curricular_id", nullable = false)
    private MatrizCurricular matriz;

    @Transient
    private List<AulaDto> aulas;

    @Transient
    private Set<String> professoresEmails;

    public Set<String> getProfessoresEmails() {
        return professoresEmails;
    }

    public void setProfessoresEmails(Set<String> professoresEmails) {
        this.professoresEmails = professoresEmails;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

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
    
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<AulaDto> getAulas() {
        return aulas;
    }

    public void setAulas(List<AulaDto> aulas) {
        this.aulas = aulas;
    }

    @Override
    public Class getLogEntity() {
        return TurmaAud.class;
    }
}
