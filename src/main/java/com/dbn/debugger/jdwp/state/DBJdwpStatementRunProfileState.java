package com.dbn.debugger.jdwp.state;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.dbn.debugger.common.state.DBStatementRunProfileState;
import com.intellij.debugger.engine.RemoteDebugProcessHandler;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class DBJdwpStatementRunProfileState extends DBStatementRunProfileState {
    public DBJdwpStatementRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        if (runner instanceof DBProgramRunner) {
            DBProgramRunner<?> programRunner = (DBProgramRunner<?>) runner;
            if (programRunner.getDebuggerType() == DBDebuggerType.JDWP) {
                Project project = getEnvironment().getProject();
                RemoteDebugProcessHandler processHandler = new RemoteDebugProcessHandler(project);
                return new DefaultExecutionResult(null, processHandler);
            }
        }

        return null;
    }
}
