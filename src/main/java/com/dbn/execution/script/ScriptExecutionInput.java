package com.dbn.execution.script;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.execution.ExecutionTarget;
import com.dbn.execution.RemoteExecutionInput;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScriptExecutionInput extends RemoteExecutionInput {
    private CmdLineInterface cmdLineInterface;
    private VirtualFile sourceFile;
    private boolean clearOutput;

    ScriptExecutionInput(Project project, VirtualFile sourceFile, ConnectionHandler connection, SchemaId targetSchema, boolean clearOutput) {
        super(project, ExecutionTarget.SCRIPT);
        this.sourceFile = sourceFile;
        setTargetConnection(connection);
        setTargetSchemaId(targetSchema);
        this.clearOutput = clearOutput;
    }

    @Override
    protected ScriptExecutionContext createExecutionContext() {
        return new ScriptExecutionContext(this);
    }

    @Override
    public ConnectionHandler getConnection() {
        return getTargetConnection();
    }

    public SchemaId getSchemaId() {
        return getTargetSchemaId();
    }
}
