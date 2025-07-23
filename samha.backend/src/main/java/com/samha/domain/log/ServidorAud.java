package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "servidor_aud")
public class ServidorAud extends BaseLogEntity {
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

    @Column(name = "matricula", updatable = false)
    private String matricula;

    @Column(name = "email", updatable = false)
    private String email;

    @OneToOne
    @JoinColumn(name = "usuario_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Usuario usuario;

    public AuditCompositeKey getPk() {
        return pk;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
