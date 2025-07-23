package com.samha.persistence.generics;

import com.samha.persistence.filter.Query;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;

public interface IQueryHelper<ENTITY, TARGET> {

    CriteriaBuilder getCriteriaBuilder();

    CriteriaQuery<TARGET> getCriteriaQuery();

    Root<ENTITY> getRoot();

    IQueryHelper<ENTITY, TARGET> where(Predicate... restrictions);

    IQueryHelper<ENTITY, TARGET> orderBy(Order... orders);

    Predicate equal(Expression<?> var1, Object var2);

    IQueryHelper<ENTITY, TARGET> select(Selection<?>... selections);

    Predicate notEqual(Expression<?> var1, Object var2);

    Predicate or(Predicate... var1);

    <T> Predicate in(Expression<? extends T> expression);

    <Y> Path<Y> get(SingularAttribute<? super ENTITY, Y> attribute);
    <Y> Join<ENTITY, Y> join(SingularAttribute<? super ENTITY, Y> attribute);

    IQueryHelper<ENTITY, TARGET> entityQuery(Query entityQuery);

    IQueryHelper<ENTITY, TARGET> distinct(boolean distinct);

    Class<ENTITY> getEntityClass();

    Class<TARGET> getTargetClass();

}
