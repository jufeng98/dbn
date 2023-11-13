package com.dbn.debugger;

import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.impl.DebuggerContextListener;
import com.intellij.debugger.impl.DebuggerSession;
import org.jetbrains.annotations.NotNull;

public class DatabaseDebuggerEventListener implements DebuggerContextListener {
    @Override
    public void changeEvent(@NotNull DebuggerContextImpl newContext, DebuggerSession.Event event) {

    }
}
