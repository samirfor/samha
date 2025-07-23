package com.samha.persistence.generics;

import com.samha.persistence.filter.EntityQueryParser;
import com.samha.persistence.filter.Query;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.Map;

public class QueryHelper<ENTITY, TARGET> implements IQueryHelper<ENTITY, TARGET>{

    private EntityManager entityManager;
    private CriteriaBuilder builder;
    private CriteriaQuery<TARGET> query;
    private Class<ENTITY> entityClass;
    private Class<TARGET> targetClass;
    private Root<ENTITY> root;

    private final EntityQueryParser<ENTITY, TARGET> queryParser;

    public QueryHelper(EntityManager entityManager, Class<ENTITY> entityClass, Class<TARGET> targetClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.targetClass = targetClass;

        this.builder = this.entityManager.getCriteriaBuilder();
        this.query = this.builder.createQuery(targetClass);
        this.root = this.query.from(entityClass);
        this.queryParser = new EntityQueryParser<>(builder, query, root, entityClass);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return this.builder;
    }

    @Override
    public CriteriaQuery getCriteriaQuery() {
        return this.query;
    }

    @Override
    public Root<ENTITY> getRoot() {
        return this.root;
    }

    @Override
    public IQueryHelper<ENTITY, TARGET> where(Predicate... restrictions) {
        if(restrictions != null && restrictions[0] != null) this.query.where(restrictions);
        return this;
    }

    @Override
    public IQueryHelper<ENTITY, TARGET> orderBy(Order... orders) {
        if (orders != null && orders[0] != null) this.query.orderBy(orders);
        return this;
    }

    @Override
    public Predicate equal(Expression<?> var1, Object var2) {
        if(var2 == null) return builder.and();
        return builder.equal(var1, var2);
    }

    @Override
    public IQueryHelper<ENTITY, TARGET> select(Selection<?>... selections) {
        query.select(builder.construct(targetClass, selections));
        return this;
    }

    @Override
    public Predicate notEqual(Expression<?> var1, Object var2) {
        if(var2 == null) return builder.and();
        return builder.notEqual(var1, var2);
    }

    @Override
    public Predicate or(Predicate... var1) {
        if(var1 == null) return builder.and();
        return builder.or(var1);
    }

    @Override
    public <T> Predicate in(Expression<? extends T> expression) {
        return builder.in(expression);
    }

    @Override
    public <Y> Path<Y> get(SingularAttribute<? super ENTITY, Y> attribute) {
        return root.get(attribute);
    }

    @Override
    public <Y> Join<ENTITY, Y> join(SingularAttribute<? super ENTITY, Y> attribute) {
        return root.join(attribute);
    }

    @Override
    public IQueryHelper<ENTITY, TARGET> entityQuery(Query entityQuery) {

        if (!isEmpty(entityQuery.getProjections()))
            query.multiselect(queryParser.buildExpressions(entityQuery.getProjections()));

        if (!isEmpty(entityQuery.getPredicates()))
            query.where(queryParser.buildPredicate(entityQuery.getPredicates()));

        if (!isEmpty(entityQuery.getGroups()))
            query.groupBy(queryParser.buildExpressions(entityQuery.getGroups()));

        if (!isEmpty(entityQuery.getOrders()))
            query.orderBy(queryParser.buildOrders(entityQuery.getOrders()));

        return this;
    }

    @Override
    public IQueryHelper<ENTITY, TARGET> distinct(boolean distinct) {
        query.distinct(distinct);
        return this;
    }

    @Override
    public Class<ENTITY> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public Class<TARGET> getTargetClass() {
        return this.targetClass;
    }

    private <T> boolean isEmpty(Collection<T> collections) {
        return collections == null || collections.isEmpty();
    }

    private <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }
}
