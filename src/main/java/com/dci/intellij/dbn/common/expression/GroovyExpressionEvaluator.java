package com.dci.intellij.dbn.common.expression;

import javax.script.*;
import java.util.Map;

import static com.dci.intellij.dbn.common.expression.SqlToGroovyExpressionConverter.cachedSqlToGroovy;
import static com.dci.intellij.dbn.common.util.Unsafe.cast;

public class GroovyExpressionEvaluator {
    private final ScriptEngine scriptEngine;

    public GroovyExpressionEvaluator() {
        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("Groovy");
    }

    public boolean evaluateBooleanExpression(String expression, Map<String, Object> bindVariables) throws ScriptException {
        Object result = evaluateExpression(expression, bindVariables);
        return cast(result);
    }

    public <T> T evaluateExpression(String expression, Map<String, Object> bindVariables) throws ScriptException {
        expression = cachedSqlToGroovy(expression);
        ScriptContext scriptContext = new SimpleScriptContext();
        bindVariables.forEach((k, v) -> scriptContext.setAttribute(k, v, ScriptContext.ENGINE_SCOPE));
        return cast(scriptEngine.eval(expression, scriptContext));
    }
}
