package com.samha.domain.log;

import com.samha.domain.BaseLogEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "menu_papeis_aud")
public class MenuPapeisAud extends BaseLogEntity {

    @EmbeddedId
    private MenuAuditCompositeKey pk;

    @Column(name = "revtype", updatable = false)
    private Integer revtype;

    @Override
    public Class getLogEntity() {
        return this.getClass();
    }

    @Embeddable
    @Data
    public class MenuAuditCompositeKey implements Serializable {
        private Long rev;
        private Long menu_id;
        private Long papeis_id;
    }


    public MenuAuditCompositeKey getPk() {
        return pk;
    }

    public void setPk(MenuAuditCompositeKey pk) {
        this.pk = pk;
    }

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }
}
