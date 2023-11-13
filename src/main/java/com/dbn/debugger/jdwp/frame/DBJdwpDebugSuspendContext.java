package com.dbn.debugger.jdwp.frame;

import com.dbn.debugger.jdwp.process.DBJdwpDebugProcess;
import com.intellij.xdebugger.frame.XSuspendContext;
import lombok.Getter;

@Getter
public class DBJdwpDebugSuspendContext extends XSuspendContext{
    private final DBJdwpDebugProcess debugProcess;
    private final XSuspendContext underlyingContext;

    @Getter(lazy = true)
    private final DBJdwpDebugExecutionStack executionStack = new DBJdwpDebugExecutionStack(this);

    public DBJdwpDebugSuspendContext(DBJdwpDebugProcess debugProcess, XSuspendContext underlyingContext) {
        this.debugProcess = debugProcess;
        this.underlyingContext = underlyingContext;
    }

    @Override
    public DBJdwpDebugExecutionStack getActiveExecutionStack() {
        return getExecutionStack();
    }
}
