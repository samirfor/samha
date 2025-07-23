package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Papel;

import javax.persistence.*;

@Entity
@Table(name = "usuario_aud")
public class UsuarioAud extends BaseLogEntity {
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }

    @EmbeddedId
    private AuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "login", updatable = false)
    private String login;

    @Column(name = "senha", updatable = false)
    private String senha;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "papel_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Papel papel;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }
}
