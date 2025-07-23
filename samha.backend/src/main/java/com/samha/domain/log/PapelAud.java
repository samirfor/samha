package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "papel_aud")
public class PapelAud extends BaseLogEntity {
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
}
