package com.dbn.debugger.jdwp.state;

import com.dbn.debugger.common.state.DBStatementRunProfileState;
import com.dbn.debugger.jdwp.DBJdwpDebugUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import org.jetbrains.annotations.NotNull;


public class DBJdwpStatementRunProfileState extends DBStatementRunProfileState {
    public DBJdwpStatementRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        return DBJdwpDebugUtil.execute(this, executor, runner);
    }
}
