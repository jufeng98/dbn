package com.dbn.common.expression;

import com.dbn.common.util.Unsafe;

import javax.script.*;
import java.util.Map;

public class GroovyExpressionEvaluator {
    private final ScriptEngine scriptEngine;

    public GroovyExpressionEvaluator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("Groovy");
    }

    public boolean evaluateBooleanExpression(String expression, Map<String, Object> bindVariables) throws ScriptException {
        Object result = evaluateExpression(expression, bindVariables);
        return Unsafe.cast(result);
    }

    public <T> T evaluateExpression(String expression, Map<String, Object> bindVariables) throws ScriptException {
        expression = SqlToGroovyExpressionConverter.cachedSqlToGroovy(expression);
        ScriptContext scriptContext = new SimpleScriptContext();
        bindVariables.forEach((k, v) -> scriptContext.setAttribute(k, v, ScriptContext.ENGINE_SCOPE));
        return Unsafe.cast(scriptEngine.eval(expression, scriptContext));
    }
}
