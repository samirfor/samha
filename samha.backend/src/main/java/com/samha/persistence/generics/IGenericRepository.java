package com.samha.persistence.generics;

import com.samha.domain.BaseLogEntity;
import com.samha.persistence.filter.Page;
import com.samha.persistence.filter.PagedList;
import org.hibernate.envers.AuditReader;

import java.io.Serializable;
import java.util.List;

public interface IGenericRepository {

    <ENTITY extends BaseLogEntity> List<ENTITY> findAll(Class<ENTITY> entityClass);

    <ENTITY extends BaseLogEntity, KEY extends Serializable> ENTITY get(Class<ENTITY> entityClass, KEY id);

    <ENTITY extends BaseLogEntity> ENTITY save(ENTITY entityClass);

    <ENTITY extends BaseLogEntity> void delete(ENTITY entityClass);

    <ENTITY extends BaseLogEntity> ENTITY update(ENTITY entity);

    <ENTITY extends BaseLogEntity> List<ENTITY> find(Class<ENTITY> entityClass, IQueryBuilder<ENTITY, ENTITY> queryBuilder);

    <ENTITY extends BaseLogEntity, TARGET> List<TARGET> find(Class<ENTITY> entityClass, Class<TARGET> targetClass, IQueryBuilder<ENTITY, TARGET> queryBuilder);

    <ENTITY extends BaseLogEntity, TARGET> PagedList<TARGET> find(Class<ENTITY> entityClass, Class<TARGET> targetClass, Page page, IQueryBuilder<ENTITY, TARGET> queryBuilder);

    <ENTITY extends BaseLogEntity> ENTITY findSingle(Class<ENTITY> entityClass, IQueryBuilder<ENTITY, ENTITY> queryBuilder);

    <ENTITY extends BaseLogEntity, TARGET> TARGET findsingle(Class<ENTITY> entityClass, Class<TARGET> targetClass, IQueryBuilder<ENTITY, TARGET> queryBuilder);

    void flush();

    AuditReader getReader();
}
