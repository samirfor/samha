package com.samha.application.commons;

import com.samha.commons.UseCase;
import com.samha.domain.BaseLogEntity;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;
import java.io.Serializable;

public class DeleteEntity<ENTITY extends BaseLogEntity, KEY extends Serializable> extends UseCase<Void> {

    private final Class<ENTITY> entityClass;
    private final KEY id;

    @Inject
    public DeleteEntity(Class<ENTITY> entityClass, KEY id) {
        this.entityClass = entityClass;
        this.id = id;
    }

    @Inject
    private IGenericRepository genericRepository;


    @Override
    protected Void execute() throws Exception {
        ENTITY toDelete = this.genericRepository.get(entityClass, id);
        this.genericRepository.delete(toDelete);
        return null;
    }
}
