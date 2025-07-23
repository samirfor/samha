package com.samha.persistence.filter;

import com.samha.commons.BusinessException;
import com.samha.persistence.generics.ExpressionHelper;
import com.samha.persistence.generics.IExpressionBuilder;
import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class EntityQueryParser<ENTITY, TARGET> {
    private static final String CURRENT_DATE_PLACEHOLDER = "${currentDate}";
    private final CriteriaBuilder builder;
    private final CriteriaQuery<TARGET> query;
    private final Root<ENTITY> root;
    private final Class<ENTITY> entityClass;
    private final Map<String, PathEntry> paths = new HashMap<>();

    private class PathEntry<T> {

        private Expression<T> path;
        private Path<T> from;
        private boolean forceLeftJoin = false;

        PathEntry(Path<T> from, String name, boolean forceLeftJoin) throws Exception {
            try {
                this.from = from;
                this.path = from.get(name);

                if (path instanceof SingularAttributePath) {
                    SingularAttribute attribute = (SingularAttribute) getPath().getModel();
                    if (attribute.isAssociation()) {
                        this.forceLeftJoin = forceLeftJoin || attribute.isOptional();
                        this.from = getFrom().join(attribute, this.forceLeftJoin ? JoinType.LEFT : JoinType.INNER);
                    } else if (attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.EMBEDDED) {
                        this.from = getPath();
                    }

                } else if (path instanceof PluralAttributePath) {
                    PluralAttribute attribute = ((PluralAttributePath) this.path).getAttribute();
                    if (attribute.isAssociation()) {
                        this.from = getFrom().join(name, JoinType.LEFT);
                        this.forceLeftJoin = true;
                    }
                }
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }

        public Path<T> getPath() {
            if (!(path instanceof Path)) throw new UnsupportedOperationException();
            return (Path<T>) path;
        }

        public <Z> From<Z, T> getFrom() {
            if (!(from instanceof From)) throw new UnsupportedOperationException();
            return (From<Z, T>) from;
        }


    }

    public EntityQueryParser(CriteriaBuilder builder, CriteriaQuery<TARGET> query, Root<ENTITY> root, Class<ENTITY> entityClass) {
        this.builder = builder;
        this.entityClass = entityClass;
        this.query = query;
        this.root = root;
    }

    private PathEntry buildPath(From root, String value) {
        String key = root.hashCode() + ".";
        if (paths.containsKey(key + value + ".")) return paths.get(key + value + ".");

        String[] names = value.split(Pattern.quote("."));
        Path from = root;
        PathEntry entry = null;

        try {
            boolean forceLeftJoin = false;
            for (String name : names) {
                key += name + ".";
                entry = paths.get(key);
                if (entry == null) paths.put(key, entry = new PathEntry(from, name, forceLeftJoin));
                from = entry.from;
                forceLeftJoin = entry.forceLeftJoin;
            }
        } catch (Exception e) {
            throw new BusinessException("Path not found | key: " + key + " | value: " + value );
        }

        if(entry == null) throw new BusinessException("Path invalid");
        return entry;
    }

    private Expression setAlias(Expression expression, String alias) {
        if (alias != null) expression.alias(alias);
        return expression;
    }

    private Expression buildExpression(From root, Object value) {

        if (value instanceof String) {
            return setAlias(this.buildPath(root, (String) value).path, (String) value);
        }

        if (value instanceof Map) {
            Map.Entry<String, Object> operatorEntry = ((Map<String, Object>) value).entrySet().iterator().next();
            String operator = operatorEntry.getKey();
            Object value2 = operatorEntry.getValue();
            Path path = this.buildPath(root, (String) value2).getPath();
            Expression expression;
//            String alias = operator + ":" + value2;
            String alias = "" + value2;

            switch (operator) {
                case Operator.Sum: expression = builder.sum(path); break;
                case Operator.Maximum: expression = builder.greatest(path); break;
                case Operator.Minimum: expression = builder.least(path); break;
                case Operator.Count: expression = builder.count(path); break;
                case Operator.CountDistinct: expression = builder.countDistinct(path); break;
                case Operator.Average: expression = builder.avg(path); break;
                //case Operator.Concat: expression =  builder.concat(); break;
                default: throw new UnsupportedOperationException("Expression with '" + operator + "' operator is not yet supported.");
            }

            return setAlias(expression, alias);
        }

        if (value instanceof IExpressionBuilder) {
            ExpressionHelper expressionHelper = new ExpressionHelper<>(builder, query, root, entityClass);
            return ((IExpressionBuilder<?, ?>) value).apply(expressionHelper);
        }

        throw new UnsupportedOperationException();
    }

    public Expression[] buildExpressions(List<Object> values) {
        Expression[] expressions = new Expression[values.size()];
        int index = 0;

        for (Object value : values) {
            expressions[index++] = this.buildExpression(this.root, value);
        }

        return expressions;
    }

    public Order[] buildOrders(List<Object> values) {
        Order[] orders = new Order[values.size()];
        int index = 0;

        for (Object value : values) {
            Expression path;
            boolean descending;

            if (value instanceof String) {
                String[] tokens = value.toString().split(Pattern.quote(" "));
                descending = tokens.length > 1 && tokens[1].equalsIgnoreCase("desc");
                path = this.buildExpression(this.root, tokens[0]);

            } else if (value instanceof Integer) {
                descending = (Integer) value < 0;
                path = this.builder.literal(abs((Integer) value));

            } else if (value instanceof Map) {
                Map map = ((Map) value);
                descending = map.containsKey("ascending") && !(boolean) map.get("ascending");
                Object mapValue = map.get("value");

                if (mapValue instanceof String)
                    path = this.buildExpression(this.root, mapValue);
                else if (mapValue instanceof Integer)
                    path = this.builder.literal((Integer) mapValue);
                else
                    throw new UnsupportedOperationException();
            } else
                throw new UnsupportedOperationException();

            orders[index++] = descending ? builder.desc(path) : builder.asc(path);
        }

        return orders;
    }

    public Predicate buildPredicate(Map<String, Object> predicatesMap) {
        return buildPredicate(this.query, this.root, predicatesMap);
    }

    private Predicate buildPredicate(AbstractQuery query, From root, Map<String, Object> predicatesMap) {
        return buildPredicate(query, root, predicatesMap.entrySet(), true);
    }

    // Build Predicate from Map Entries
    private Predicate buildPredicate(AbstractQuery query, From root, Collection<Map.Entry<String, Object>> entries, boolean isConjunction) {
        Predicate predicate = null;

        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Predicate referencePredicate;

            switch (key) {
                case Operator.And: referencePredicate = buildPredicate(query, root, getPredicateEntries(entry), true); break;
                case Operator.Or: referencePredicate = buildPredicate(query, root, getPredicateEntries(entry), false); break;
                default: referencePredicate = buildPredicate(query, root, entry); break;
            }

            if (predicate == null)
                predicate = referencePredicate;
            else if (isConjunction)
                predicate = builder.and(predicate, referencePredicate);
            else
                predicate = builder.or(predicate, referencePredicate);
        }

        return predicate;
    }

    private Collection<Map.Entry<String, Object>> getPredicateEntries(Map.Entry<String, Object> entry) {
        List<Map.Entry<String, Object>> entries = new ArrayList<>();
        Object value = entry.getValue();

        if (value instanceof Map)
            entries.addAll(((Map<String, Object>) value).entrySet());
        else if (value instanceof List)
            for (Map<String, Object> item : (List<Map<String, Object>>) value)
                entries.addAll(item.entrySet());
        else
            throw new UnsupportedOperationException();

        return entries;
    }

    // Build Predicate from Map Entry  Converter Entrada do Mapa em Predicado com Base no Operador
    private Predicate buildPredicate(AbstractQuery query, From root, Map.Entry<String, Object> entry) {

        Map.Entry<String, Object> operatorEntry = ((Map<String, Object>) entry.getValue()).entrySet().iterator().next();
        String propertyPath = entry.getKey();
        String operator = operatorEntry.getKey();
        Object value = operatorEntry.getValue();

        //Expression path = this.buildPath(root, propertyPath).path;
        Expression path = this.buildExpression(root, propertyPath);

        if (value instanceof Map) {
            boolean isProperty = ((Map) value).containsKey("isProperty") && (boolean) ((Map) value).get("isProperty");
            value = ((Map) value).get("value");
            if (isProperty) {
                if (value instanceof List) {
                    Expression[] valuePaths = ((List<?>) value).stream().map(v -> this.buildPath(root, v.toString()).path).toArray(Expression[]::new);
                    return this.buildPredicate(operator, path, valuePaths);
                }

                Expression valuePath = this.buildPath(root, value.toString()).path;
                return this.buildPredicate(operator, path, valuePath);
            }
        }

        return this.buildPredicate( operator, path, this.convertValue(value, path.getJavaType()));
    }
    private Predicate buildPredicate(String operator, Expression path, Object value) {
        switch (operator) {
            case Operator.Contains: return builder.like(path.as(String.class), "%" + value + "%");
//            case Operator.StartsWith: return builder.like(path.as(String.class), value + "%");
//            case Operator.NotStartsWith: return builder.notLike(path.as(String.class), value + "%");
//            case Operator.EndsWith: return builder.like(path.as(String.class), "%" + value);
            case Operator.NotContains: return builder.notLike(path.as(String.class), "%" + value + "%");
            case Operator.Equals: return value == null ? builder.isNull(path) : builder.equal(path, value);
            case Operator.NotEquals: return value == null ? builder.isNotNull(path) : builder.notEqual(path, value);
            case Operator.GreaterThan: return builder.greaterThan(path, (Comparable) value);
//            case Operator.GreaterThanOrEqual: return builder.greaterThanOrEqualTo(path, (Comparable) value);
            case Operator.LessThan: return builder.lessThan(path, (Comparable) value);
//            case Operator.LessThanOrEqual: return builder.lessThanOrEqualTo(path, (Comparable) value);
            case Operator.Between: return builder.between(path, ((List<Comparable>) value).get(0), ((List<Comparable>) value).get(1));
            case Operator.In: return path.in(value);
            case Operator.NotIn: return builder.not(path.in(value));
            default: throw new UnsupportedOperationException("Binary predicate with the '"+ operator + "' operator is not yet supported.");
        }
    }

    private Predicate buildPredicate(String operator, Expression path, Expression value) {
        switch (operator) {
            case Operator.Contains: return builder.like(path.as(String.class), builder.concat(builder.concat("%", value.as(String.class)), "%"));
//            case Operator.StartsWith: return builder.like(path.as(String.class), builder.concat(value.as(String.class), "%"));
//            case Operator.NotStartsWith: return builder.notLike(path.as(String.class), builder.concat(value.as(String.class), "%"));
//            case Operator.EndsWith: return builder.like(path.as(String.class),  builder.concat("%", value.as(String.class)));
            case Operator.NotContains: return builder.notLike(path.as(String.class), builder.concat(builder.concat("%", value.as(String.class)), "%"));
            case Operator.Equals: return builder.equal(path, value);
            case Operator.NotEquals: return builder.notEqual(path, value);
            case Operator.GreaterThan: return builder.greaterThan(path, value);
//            case Operator.GreaterThanOrEqual: return builder.greaterThanOrEqualTo(path, value);
            case Operator.LessThan: return builder.lessThan(path, value);
//            case Operator.LessThanOrEqual: return builder.lessThanOrEqualTo(path, value);
            default: throw new UnsupportedOperationException("Property comparison with '" + operator + "' operator is not yet supported.");
        }
    }


    private Predicate buildPredicate(String operator, Expression path, Expression[] values) {
        switch (operator) {
            case Operator.Between: return builder.between(path, values[0], values[1]);
            case Operator.In: return path.in(values);
            case Operator.NotIn: return builder.not(path.in(values));
            default: throw new UnsupportedOperationException("Properties comparison with '" + operator + "' operator is not yet supported.");
        }
    }

    private Object convertValue(Object value, Class targetType) {

        if (value == null) return null;

        Class<?> sourceType = value.getClass();

        if (sourceType.equals(targetType)) return value; // Shortcut

        if (value instanceof ArrayList) {
            ArrayList values = (ArrayList) value;
            for (int i = 0; i < values.size(); i++) {
                values.set(i, convertValue(values.get(i), targetType));
            }
            return values;
        }

        if (value instanceof Map) throw new UnsupportedOperationException();

        if (targetType.isEnum() && !sourceType.isEnum()) {
            try {
                return Enum.valueOf(targetType, value.toString());
            } catch (Exception e) {
                return null;
            }
        }

        if (targetType.equals(Timestamp.class) && !sourceType.equals(Timestamp.class)) {
            try {
                if (value instanceof String) {
                    if(CURRENT_DATE_PLACEHOLDER.equalsIgnoreCase((String) value)) return Timestamp.from(Instant.now());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                    LocalDateTime dateTime = LocalDateTime.parse((String) value, formatter);
                    return Timestamp.valueOf(dateTime);
                } else {
                    return new Timestamp((long) value);
                }
            } catch (Exception e) {
                return null;
            }
        }

        if (targetType.equals(ZonedDateTime.class) && !sourceType.equals(ZonedDateTime.class)) {
            try {
                if (value instanceof String) {
                    if(CURRENT_DATE_PLACEHOLDER.equalsIgnoreCase((String) value)) return ZonedDateTime.now();
                    return ZonedDateTime.parse((String) value);
                } else
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) value), ZoneId.systemDefault());
            } catch (Exception e) {
                return null;
            }
        }

        if (targetType.equals(LocalDate.class) && !sourceType.equals(LocalDate.class)) {
            try {
                if(CURRENT_DATE_PLACEHOLDER.equalsIgnoreCase((String) value)) return LocalDate.now();
                return LocalDate.parse(((String) value).substring(0, 10));
            } catch (Exception e) {
                return null;
            }
        }

        return value;
    }
}
