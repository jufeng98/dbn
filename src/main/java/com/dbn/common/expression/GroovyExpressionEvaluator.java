package com.dbn.common.expression;

import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.Objects;

import static com.dbn.common.expression.SqlToGroovyExpressionConverter.cachedSqlToGroovy;
import static com.dbn.common.expression.SqlToGroovyExpressionConverter.sqlToGroovy;
import static com.dbn.common.util.Unsafe.cast;

@Slf4j
public class GroovyExpressionEvaluator implements ExpressionEvaluator{
    private final ScriptEngine scriptEngine;

    public GroovyExpressionEvaluator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("Groovy");
    }

    @Override
    public boolean isValidExpression(String expression, ExpressionEvaluatorContext context) {
        try {
            evaluate(expression, context);
            return true;
        } catch (ScriptException e) {
            return false;
        }
    }

    @Override
    public boolean isValidExpression(String expression, Class<?> expectedOutcome, ExpressionEvaluatorContext context) {
        try {
            Object result = evaluate(expression, context);

            return result == null || expectedOutcome.isAssignableFrom(result.getClass());
        } catch (ScriptException e) {
            return false;
        }
    }

    @Override
    public <T> T evaluateExpression(String expression, ExpressionEvaluatorContext context) {
        try {
            return evaluate(expression, context);
        } catch (Exception e) {
            log.error("Failed to evaluate expression: {}", expression, e);
            return null;
        }
    }

    @Override
    public boolean evaluateBooleanExpression(String expression, ExpressionEvaluatorContext context) {
        Object result = evaluateExpression(expression, context);
        return result == null || Objects.equals(result, Boolean.TRUE);
    }

    private <T> T evaluate(String expression, ExpressionEvaluatorContext context) throws ScriptException {
        try {
            expression = context.isTemporary() ? sqlToGroovy(expression) : cachedSqlToGroovy(expression);
            context.setEvaluatedExpression(expression);
            context.setEvaluationError(null);

            ScriptContext scriptContext = context.createScriptContext();
            Object result = scriptEngine.eval(expression, scriptContext);

            return cast(result);
        } catch (Throwable e) {
            context.setEvaluationError(null);
            throw e;
        }
    }
}
