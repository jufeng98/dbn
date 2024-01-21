package com.dbn.debugger.jdwp.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBDebugProcessStarter;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.execution.statement.StatementExecutionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBStatementJdwpRunner extends DBProgramRunner<StatementExecutionInput> {
    public static final String RUNNER_ID = "DBNStatementJdwpRunner";

    @Override
    @NotNull
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public DBDebuggerType getDebuggerType() {
        return DBDebuggerType.JDWP;
    }

    @Override
    protected DBDebugProcessStarter createProcessStarter(ConnectionHandler connection) {
        if (connection.isCloudDatabase() || connection.getSettings().getDebuggerSettings().isTcpDriverTunneling()) {
            return new DBStatementJdwpCloudProcessStarter(connection);
        }
        return new DBStatementJdwpLocalProcessStarter(connection);
    }

    @Override
    protected void promptExecutionDialog(StatementExecutionInput executionInput, Runnable callback) {
        Project project = executionInput.getProject();
        StatementExecutionManager executionManager = StatementExecutionManager.getInstance(project);
        executionManager.promptExecutionDialog(executionInput.getExecutionProcessor(), DBDebuggerType.JDWP, callback);
    }
}

