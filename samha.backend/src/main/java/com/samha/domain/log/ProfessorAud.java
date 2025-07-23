package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import com.samha.domain.Coordenadoria;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "professor_aud")
public class ProfessorAud extends BaseLogEntity{
    @Override
    public Class getLogEntity() {
        return this.getClass();
    }
    @EmbeddedId
    private ProfessorAuditPK pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Column(name = "carga_horaria", updatable = false)
    private Double cargaHoraria;

    @Column(name = "ativo", updatable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordenadoria_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Coordenadoria coordenadoria;

    @Embeddable
    @Data
    class ProfessorAuditPK implements Serializable{
        private Long rev;
        private Long professor_id;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

    public ProfessorAuditPK getPk() {
        return pk;
    }

    public void setPk(ProfessorAuditPK pk) {
        this.pk = pk;
    }

    public Double getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Coordenadoria getCoordenadoria() {
        return coordenadoria;
    }

    public void setCoordenadoria(Coordenadoria coordenadoria) {
        this.coordenadoria = coordenadoria;
    }
}
