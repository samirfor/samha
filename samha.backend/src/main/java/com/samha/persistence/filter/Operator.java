package com.samha.persistence.filter;

/**
 * Esta classe deve ser um espelho direto da interface Filter no frontend
 */
public class Operator {
    /*
    equals?: any;
  notEquals?: any;
  contains?: any;
  notContains?: any;
  in?: any[];
  notIn?: any[];
  greaterThan?: any;
  lessThan?: any;
  between?: any;
  and?: Predicate;
  or?: Predicate;
     */
    public static final String Equals = "equals";
    public static final String NotEquals = "notEquals";
    public static final String Contains = "contains";
    public static final String NotContains = "notContains";
    public static final String In = "in";
    public static final String NotIn = "notIn";
    public static final String GreaterThan = "greaterThan";
    public static final String LessThan = "lessThan";
    public static final String Between = "between";
    public static final String And = "and";
    public static final String Or = "or";

    public static final String Count = "count";
    public static final String CountDistinct = "countDistinct";
    public static final String Sum = "sum";
    public static final String Maximum = "max";
    public static final String Minimum = "min";
    public static final String Average = "avg";
    public static final String Concat = "concat";
}
