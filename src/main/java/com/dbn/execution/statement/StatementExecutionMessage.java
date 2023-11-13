package com.dbn.execution.statement;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionId;
import com.dbn.execution.common.message.ConsoleMessage;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.execution.statement.result.StatementExecutionResult;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatementExecutionMessage extends ConsoleMessage {
    private final String causeMessage;
    private StatementExecutionResult executionResult;
    private final ConnectionId connectionId;

    public StatementExecutionMessage(StatementExecutionResult executionResult, String message, String causeMessage, MessageType messageType) {
        super(messageType, message);
        this.executionResult = executionResult;
        this.causeMessage = causeMessage;
        this.connectionId = executionResult.getConnectionId();
    }

    public VirtualFile getVirtualFile() {
        VirtualFile virtualFile = executionResult.getExecutionProcessor().getVirtualFile();
        return Failsafe.nn(virtualFile);
    }

    public boolean isOrphan() {
        StatementExecutionProcessor executionProcessor = executionResult.getExecutionProcessor();
        return executionProcessor.isDirty() ||
                executionProcessor.getExecutionResult() != executionResult; // overwritten result
    }

    @Override
    public boolean isNew() {
        return super.isNew()/* && !isOrphan()*/;
    }

    public void createStatementViewer() {
        
    }

    @Override
    public void disposeInner() {
        Disposer.dispose(executionResult);
        super.disposeInner();
    }
}
