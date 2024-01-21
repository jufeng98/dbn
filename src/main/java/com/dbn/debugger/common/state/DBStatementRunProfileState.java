package com.dbn.debugger.common.state;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import org.jetbrains.annotations.NotNull;


public abstract class DBStatementRunProfileState extends DBRunProfileState {
    public DBStatementRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        return null;
    }
}
