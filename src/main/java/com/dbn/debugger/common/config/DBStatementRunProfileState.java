package com.dbn.debugger.common.config;

import com.dbn.debugger.DBDebuggerType;
import com.dbn.debugger.common.process.DBProgramRunner;
import com.intellij.debugger.engine.RemoteDebugProcessHandler;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class DBStatementRunProfileState extends DBRunProfileState {
    public DBStatementRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        if (runner instanceof DBProgramRunner) {
            // TODO check why this is needed for jdwp. Is this ever invoked?
            DBProgramRunner programRunner = (DBProgramRunner) runner;
            if (programRunner.getDebuggerType() == DBDebuggerType.JDWP) {
                Project project = getEnvironment().getProject();
                RemoteDebugProcessHandler processHandler = new RemoteDebugProcessHandler(project);
                return new DefaultExecutionResult(null, processHandler);
            }
        }

        return null;
    }
}
