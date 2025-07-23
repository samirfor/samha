package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Papel;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "menu_aud")
public class MenuAud extends BaseLogEntity {
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }
    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "nome", updatable = false)
    private String nome;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "papel_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Set<Papel> papeis;

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

    public Set<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<Papel> papeis) {
        this.papeis = papeis;
    }
}
