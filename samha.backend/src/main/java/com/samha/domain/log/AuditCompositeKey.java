package com.samha.domain.log;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class AuditCompositeKey implements Serializable {
    private static final long serialVersionUID = -8619406593704953837L;
    private Long id;
    private Long rev;

    public AuditCompositeKey(Long id, Long rev) {
        this.id = id;
        this.rev = rev;
    }

    public AuditCompositeKey() {}
}
