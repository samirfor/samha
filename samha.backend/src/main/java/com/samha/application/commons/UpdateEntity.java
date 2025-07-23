package com.samha.application.commons;

import com.samha.commons.UseCase;
import com.samha.domain.BaseLogEntity;
import com.samha.persistence.generics.IGenericRepository;

import javax.inject.Inject;


public class UpdateEntity<ENTITY extends BaseLogEntity> extends UseCase<ENTITY> {

    private final ENTITY entityClass;

    public UpdateEntity(ENTITY entityClass){
        this.entityClass = entityClass;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Override
    protected ENTITY execute() throws Exception {
        return genericRepository.update(entityClass);
    }
}
