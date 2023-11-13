package com.dbn.execution.statement;

import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

public interface StatementExecutionListener extends EventListener {
    Topic<StatementExecutionListener> TOPIC = Topic.create("Statement execution event", StatementExecutionListener.class);
    void statementExecuted(StatementExecutionProcessor processor);
}
