package com.dbn.debugger.jdbc.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBDebugProcessStarter;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdbcRunner extends DBProgramRunner<MethodExecutionInput> {
    public static final String RUNNER_ID = "DBNMethodRunner";

    @Override
    @NotNull
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public DBDebuggerType getDebuggerType() {
        return DBDebuggerType.JDBC;
    }

    @Override
    protected DBDebugProcessStarter createProcessStarter(ConnectionHandler connection) {
        return new DBMethodJdbcProcessStarter(connection);
    }

    @Override
    protected void promptExecutionDialog(MethodExecutionInput executionInput, Runnable callback) {
        Project project = executionInput.getProject();
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        executionManager.promptExecutionDialog(executionInput, DBDebuggerType.JDBC, callback);
    }
}

