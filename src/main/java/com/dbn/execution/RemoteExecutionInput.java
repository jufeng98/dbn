package com.dbn.execution;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.intellij.openapi.project.Project;

public abstract class RemoteExecutionInput extends ExecutionInput{
    public RemoteExecutionInput(Project project, ExecutionTarget executionTarget) {
        super(project, executionTarget);
    }

    @Override
    public ConnectionId getConnectionId() {
        ConnectionHandler connection = getConnection();
        return connection == null ? null : connection.getConnectionId();
    }
}
