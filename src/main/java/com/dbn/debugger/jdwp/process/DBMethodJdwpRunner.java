package com.dbn.debugger.jdwp.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBDebugProcessStarter;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.execution.method.MethodExecutionManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdwpRunner extends DBProgramRunner<MethodExecutionInput> {
    public static final String RUNNER_ID = "DBNMethodJdwpRunner";

    @Override
    @NotNull
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    protected DBDebugProcessStarter createProcessStarter(ConnectionHandler connection) {
        if(connection.isCloudDatabase() || connection.getSettings().getDebuggerSettings().isTcpDriverTunneling()){
            return new DBMethodJdwpCloudProcessStarter(connection);
        }
        return new DBMethodJdwpLocalProcessStarter(connection);
    }

    @Override
    protected void promptExecutionDialog(MethodExecutionInput executionInput, Runnable callback) {
        Project project = executionInput.getProject();
        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        executionManager.promptExecutionDialog(executionInput, DBDebuggerType.JDWP, callback);
    }
}

