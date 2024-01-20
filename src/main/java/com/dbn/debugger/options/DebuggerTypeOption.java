package com.dbn.debugger.options;

import com.dbn.common.option.InteractiveOption;
import com.dbn.debugger.DBDebuggerType;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Getter
public enum DebuggerTypeOption implements InteractiveOption {
    JDBC("Classic (over JDBC)", DBDebuggerType.JDBC),
    JDWP("JDWP (over TCP)", DBDebuggerType.JDWP),
    ASK("Ask"),
    CANCEL("Cancel");

    private final String name;
    private final DBDebuggerType debuggerType;

    DebuggerTypeOption(String name) {
        this.name = name;
        this.debuggerType = null;
    }

    DebuggerTypeOption(String name, DBDebuggerType debuggerType) {
        this.name = name;
        this.debuggerType = debuggerType;
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
