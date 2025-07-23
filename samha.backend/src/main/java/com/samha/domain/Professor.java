package com.samha.domain;

import com.samha.domain.dto.AulaDto;
import com.samha.domain.log.ProfessorAud;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Audited
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "professor_id")
public class Professor extends Servidor {
    @Column(nullable = false)
    private Double cargaHoraria;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "coordenadoria_id", nullable = false)
    private Coordenadoria coordenadoria;

    @Transient
    private List<AulaDto> aulas;

    @Transient
    private String cargaHorariaCalculada;

    public String obterNomeAbreviado(){

        int espaco = this.getNome().indexOf(" ");

        if(espaco > 0){

            String nomeAbreviado = this.getNome().substring(0, espaco) + " ";

            for(int indice = espaco; indice < this.getNome().length() - 1; indice++){
                char caractere = this.getNome().charAt(indice);
                if(caractere == ' '){
                    char letra = this.getNome().charAt(indice + 1);
                    if(letra != 'd')
                        nomeAbreviado = nomeAbreviado + letra;
                }
            }

            return nomeAbreviado;
        }
        return getNome();
    }

    public Double getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Coordenadoria getCoordenadoria() {
        return coordenadoria;
    }

    public void setCoordenadoria(Coordenadoria coordenadoria) {
        this.coordenadoria = coordenadoria;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public Class getLogEntity() {
        return ProfessorAud.class;
    }

    public List<AulaDto> getAulas() {
        return aulas;
    }

    public void setAulas(List<AulaDto> aulas) {
        this.aulas = aulas;
    }

    public String getCargaHorariaCalculada() {
        return cargaHorariaCalculada;
    }

    public void setCargaHorariaCalculada(String cargaHorariaCalculada) {
        this.cargaHorariaCalculada = cargaHorariaCalculada;
    }
}
