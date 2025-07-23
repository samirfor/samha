package com.samha.persistence.generics;

import javax.persistence.criteria.Expression;
import java.util.function.Function;

@FunctionalInterface
public interface IExpressionBuilder<SOURCE, TARGET> extends Function<ExpressionHelper<SOURCE, TARGET>, Expression<TARGET>> {

}
