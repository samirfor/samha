package com.samha.domain.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "revinfo")
public class RevInfo {

    @Id
    @Column(columnDefinition = "bigint")
    private Long rev;

    @Column
    private Long revtstmp;

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public Long getRevtstmp() {
        return revtstmp;
    }

    public void setRevtstmp(Long revtstmp) {
        this.revtstmp = revtstmp;
    }
}
