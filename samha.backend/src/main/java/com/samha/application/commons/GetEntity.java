package com.samha.application.commons;

import com.samha.commons.UseCase;
import com.samha.domain.BaseLogEntity;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.io.Serializable;

public class GetEntity<ENTITY extends BaseLogEntity, KEY extends Serializable> extends UseCase<ENTITY> {

    private final Class<ENTITY> entityClass;
    private final KEY id;

    @Inject
    protected IGenericRepository genericRepository;

    public GetEntity(Class<ENTITY> entityClass, KEY id) {
        this.entityClass = entityClass;
        this.id = id;
    }

    @Override
    protected ENTITY execute() throws Exception {
        return genericRepository.get(this.entityClass, this.id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "." + entityClass.getSimpleName();
    }
}
