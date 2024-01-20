package com.dbn.debugger.jdwp.config;

import com.dbn.debugger.common.config.DBRunProfileState;
import com.intellij.debugger.engine.RemoteDebugProcessHandler;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class DBStatementJdwpRunProfileState extends DBRunProfileState {
    public DBStatementJdwpRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        Project project = getEnvironment().getProject();
        RemoteDebugProcessHandler processHandler = new RemoteDebugProcessHandler(project);
        return new DefaultExecutionResult(null, processHandler);
    }
}
