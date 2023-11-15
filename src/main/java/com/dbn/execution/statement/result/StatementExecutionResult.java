package com.dbn.execution.statement.result;

import com.dbn.common.message.MessageType;
import com.dbn.common.navigation.NavigationInstructions;
import com.dbn.database.DatabaseMessage;
import com.dbn.execution.ExecutionResult;
import com.dbn.execution.compiler.CompilerResult;
import com.dbn.execution.statement.StatementExecutionContext;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.execution.statement.StatementExecutionMessage;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.execution.statement.result.ui.StatementExecutionResultForm;

public interface StatementExecutionResult extends ExecutionResult<StatementExecutionResultForm> {
    StatementExecutionProcessor getExecutionProcessor();
    StatementExecutionMessage getExecutionMessage();
    StatementExecutionInput getExecutionInput();
    StatementExecutionContext getExecutionContext();

    StatementExecutionStatus getExecutionStatus();

    void setExecutionStatus(StatementExecutionStatus executionStatus);
    void updateExecutionMessage(MessageType messageType, String message, DatabaseMessage databaseMessage);
    void updateExecutionMessage(MessageType messageType, String message);
    void clearExecutionMessage();
    void calculateExecDuration();
    int getExecutionDuration();



    void navigateToEditor(NavigationInstructions instructions);

    int getUpdateCount();

    CompilerResult getCompilerResult();
    boolean hasCompilerResult();
    boolean isBulkExecution();

    String getLoggingOutput();
    void setLoggingOutput(String loggerOutput);
    boolean isLoggingActive();
    void setLoggingActive(boolean databaseLogActive);
}
