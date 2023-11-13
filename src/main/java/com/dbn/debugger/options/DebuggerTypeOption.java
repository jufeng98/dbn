package com.dbn.debugger.options;

import com.dbn.common.option.InteractiveOption;
import com.dbn.debugger.DBDebuggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public enum DebuggerTypeOption implements InteractiveOption {
    JDBC("Classic (over JDBC)", DBDebuggerType.JDBC),
    JDWP("JDWP (over TCP)", DBDebuggerType.JDWP),
    ASK("Ask"),
    CANCEL("Cancel");

    private String name;
    private DBDebuggerType debuggerType;

    DebuggerTypeOption(String name) {
        this.name = name;
    }

    DebuggerTypeOption(String name, DBDebuggerType debuggerType) {
        this.name = name;
        this.debuggerType = debuggerType;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public DBDebuggerType getDebuggerType() {
        return debuggerType;
    }

    @Override
    public boolean isCancel() {
        return this == CANCEL;
    }

    @Override
    public boolean isAsk() {
        return this == ASK;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }
}
