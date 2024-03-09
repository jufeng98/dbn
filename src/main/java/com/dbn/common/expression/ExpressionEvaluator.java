package com.dbn.common.expression;

public interface ExpressionEvaluator {

    boolean isValidExpression(String expression, ExpressionEvaluatorContext context);

    boolean isValidExpression(String expression, Class<?> expectedOutcome, ExpressionEvaluatorContext context);

    boolean evaluateBooleanExpression(String expression, ExpressionEvaluatorContext context);

    <T> T evaluateExpression(String expression, ExpressionEvaluatorContext context);
}
