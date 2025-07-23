package com.samha.domain;

import com.samha.domain.log.CoordenadoriaAud;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
@Table(name = "coordenadoria")
public class Coordenadoria extends BaseLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nome;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eixo_id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Eixo eixo;

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

    public Eixo getEixo() {
        return eixo;
    }

    public void setEixo(Eixo eixo) {
        this.eixo = eixo;
    }

    @Override
    public Class getLogEntity() {
        return CoordenadoriaAud.class;
    }
}
