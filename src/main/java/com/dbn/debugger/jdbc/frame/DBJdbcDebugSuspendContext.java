package com.dbn.debugger.jdbc.frame;

import com.dbn.debugger.jdbc.DBJdbcDebugProcess;
import com.intellij.xdebugger.frame.XSuspendContext;
import lombok.Getter;

public class DBJdbcDebugSuspendContext extends XSuspendContext{
    private final DBJdbcDebugProcess debugProcess;

    @Getter(lazy = true)
    private final DBJdbcDebugExecutionStack executionStack = new DBJdbcDebugExecutionStack(debugProcess);

    public DBJdbcDebugSuspendContext(DBJdbcDebugProcess debugProcess) {
        this.debugProcess = debugProcess;
    }

    @Override
    public DBJdbcDebugExecutionStack getActiveExecutionStack() {
        return getExecutionStack();
    }
}
