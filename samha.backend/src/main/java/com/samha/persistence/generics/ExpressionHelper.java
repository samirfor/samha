package com.samha.persistence.generics;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.function.Function;

public class ExpressionHelper<S, T> {

    public final CriteriaBuilder builder;
    public final CriteriaQuery<T> query;
    public final From<S, S> root;
    private final Class<S> sourceClass;
    private String alias;

    public ExpressionHelper(CriteriaBuilder builder, CriteriaQuery<T> query, From<S, S> root, Class<S> sourceClass) {
        this.builder = builder;
        this.query = query;
        this.root = root;
        this.sourceClass = sourceClass;
    }

    public <TARGET> ExpressionHelper(IQueryHelper<S, TARGET> queryHelper) {
        this.builder = queryHelper.getCriteriaBuilder();
        this.query = (CriteriaQuery<T>) queryHelper.getCriteriaQuery();
        this.root = queryHelper.getRoot();
        this.sourceClass = queryHelper.getEntityClass();
    }

    public ExpressionHelper<S, T> alias(String alias) {
        this.alias = alias;
        return this;
    }

    public <X> Path<X> get(SingularAttribute<S, X> attribute) {
        return root.get(attribute);
    }

    private <X> Expression<X> setAlias(Expression<X> expression) {
        if (alias != null) expression.alias(alias);
        return expression;
    }

    public <B> Expression<T> count(Class<B> childClass, Function<Root<B>, Path<S>> parentPath, Function<Root<B>, Predicate> filterPredicate) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<B> subroot = subquery.from(childClass);
        subquery.select(builder.count(subroot));
        Predicate basePredicate = builder.equal(root.get("id"), parentPath.apply(subroot).get("id"));
        subquery = filterPredicate == null ? subquery.where(basePredicate) : subquery.where(basePredicate, filterPredicate.apply(subroot));

        return setAlias((Expression<T>) subquery.getSelection());
    }

    public <B> Expression<T> count(Class<B> childClass, Function<Root<B>, Path<S>> parentPath) {
        return count(childClass, parentPath, null);
    }

    public Expression<T> concat(String separator, Path<?>... paths) {
        Expression expression = paths[0].as(String.class);
        for (int i = 1; i < paths.length; i++) {
            expression = builder.concat(expression, builder.concat(separator, paths[i].as(String.class)));
        }

        return setAlias(expression);
    }

}
