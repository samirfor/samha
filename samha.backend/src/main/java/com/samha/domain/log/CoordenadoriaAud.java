package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Eixo;

import javax.persistence.*;

@Entity
@Table(name = "coordenadoria_aud")
public class CoordenadoriaAud extends BaseLogEntity {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eixo_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Eixo eixo;

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

    public Eixo getEixo() {
        return eixo;
    }

    public void setEixo(Eixo eixo) {
        this.eixo = eixo;
    }
}
