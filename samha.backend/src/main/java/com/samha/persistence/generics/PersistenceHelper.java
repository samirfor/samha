package com.samha.persistence.generics;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

/**
 * Utility class for dealing with JPA API*
 * Reference: https://github.com/chelu/jdal/blob/master/core/src/main/java/org/jdal/dao/jpa/JpaUtils.java
 */
public abstract class PersistenceHelper {

    private static volatile int aliasCount = 0;

    /**
     * Result count from a CriteriaQuery
     *
     * @param entityManager Entity Manager
     * @param criteria      Criteria Query to count results
     * @return row count
     */
    public static <T> Long count(EntityManager entityManager, CriteriaQuery<T> criteria) {
        return entityManager.createQuery(countCriteria(entityManager, criteria)).getSingleResult();
    }

    /**
     * Create a row count CriteriaQuery from a CriteriaQuery
     *
     * @param entityManager entity manager
     * @param criteria      source criteria
     * @return row count CriteriaQuery
     */
    public static <T> CriteriaQuery<Long> countCriteria(EntityManager entityManager, CriteriaQuery<T> criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);

        if (criteria.getGroupList().size() > 0) {
            return countCriteriaWithGroupBy(entityManager, criteria);
        }
        copyCriteriaWithoutSelectionAndOrder(criteria, countCriteria, false);

        Root<?> root = countCriteria.getRoots().iterator().next(); // TODO: Handle multiple roots
        Expression<Long> countExpression = criteria.isDistinct() ? builder.countDistinct(root) : builder.count(root);
        return countCriteria.select(countExpression);
    }


    /**
     * Create a row count CriteriaQuery from a CriteriaQuery with GroupBy
     *
     * @param entityManager entity manager
     * @param criteria      source criteria
     * @return row count CriteriaQuery
     */
    public static <T> CriteriaQuery<Long> countCriteriaWithGroupBy(EntityManager entityManager, CriteriaQuery<T> criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);

        Root root = criteria.getRoots().iterator().next();
        Root from = countCq.from(root.getJavaType());
        countCq.select(cb.countDistinct(from));

        Subquery subquery = countCq.subquery(root.getJavaType());
        for (Root<?> subroot : criteria.getRoots()) {
            Root<?> dest = subquery.from(subroot.getJavaType());
            dest.alias(getOrCreateAlias(subroot));
            copyJoins(subroot, dest);

            subquery.select(subroot);
        }

        if (criteria.getRestriction() != null) {
            subquery.where(criteria.getRestriction(), cb.equal(root.get("id"), from.get("id")));
        }
        if (criteria.getGroupRestriction() != null) {
            subquery.having(criteria.getGroupRestriction());
        }

        subquery.groupBy(criteria.getGroupList());

        countCq.where(cb.exists(subquery));
        return countCq;
    }

    /**
     * Gets The result alias, if none set a default one and return it
     *
     * @param selection
     * @return root alias or generated one
     */
    public static synchronized <T> String getOrCreateAlias(Selection<T> selection) {
        // reset alias count
        if (aliasCount > 1000)
            aliasCount = 0;

        String alias = selection.getAlias();
        if (alias == null) {
            alias = "alias" + aliasCount++;
            selection.alias(alias);
        }
        return alias;
    }

    /**
     * Find the Root with type class on CriteriaQuery Root Set
     *
     * @param <T>   root type
     * @param query criteria query
     * @param clazz root type
     * @return Root<T> of null if none
     */
    @SuppressWarnings("unchecked")
    public static <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

        for (Root<?> r : query.getRoots()) {
            if (clazz.equals(r.getJavaType())) {
                return (Root<T>) r.as(clazz);
            }
        }

        throw new NullPointerException();
    }

    /**
     * Copy criteria without selection and order.
     *
     * @param from source Criteria.
     * @param to   destination Criteria.
     */
    private static void copyCriteriaWithoutSelectionAndOrder(CriteriaQuery<?> from, CriteriaQuery<?> to, boolean copyFetches) {

        // Copy Roots
        for (Root<?> root : from.getRoots()) {
            Root<?> dest = to.from(root.getJavaType());
            dest.alias(getOrCreateAlias(root));
            copyJoins(root, dest);
            if (copyFetches) copyFetches(root, dest);
        }

        to.groupBy(from.getGroupList());
        to.distinct(from.isDistinct());
        if (from.getGroupRestriction() != null) to.having(from.getGroupRestriction());
        Predicate predicate = from.getRestriction();
        if (predicate != null) to.where(predicate);
    }

    /**
     * Copy Joins
     *
     * @param from source Join
     * @param to   destination Join
     */
    public static void copyJoins(From<?, ?> from, From<?, ?> to) {
        for (Join<?, ?> j : from.getJoins()) {
            Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
            toJoin.alias(getOrCreateAlias(j));
            copyJoins(j, toJoin);
        }
    }

    /**
     * Copy Fetches
     *
     * @param from source From
     * @param to   destination From
     */
    public static void copyFetches(From<?, ?> from, From<?, ?> to) {
        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            copyFetches(f, toFetch);
        }
    }

    /**
     * Copy Fetches
     *
     * @param from source Fetch
     * @param to   dest Fetch
     */
    public static void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            // recursively copy fetches
            copyFetches(f, toFetch);
        }
    }
}
