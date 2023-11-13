package com.dbn.debugger.jdbc.frame;

import com.dbn.debugger.common.frame.DBDebugValue;
import com.dbn.debugger.jdbc.DBJdbcDebugProcess;
import com.intellij.xdebugger.frame.XValueModifier;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class DBJdbcDebugValue extends DBDebugValue<DBJdbcDebugStackFrame>{
    private DBJdbcDebugValueModifier modifier;

    DBJdbcDebugValue(DBJdbcDebugStackFrame stackFrame, DBJdbcDebugValue parentValue, String variableName, @Nullable List<String> childVariableNames, Icon icon) {
        super(stackFrame, variableName, childVariableNames, parentValue, icon);
    }

    @Override
    public DBJdbcDebugProcess getDebugProcess() {
        return (DBJdbcDebugProcess) super.getDebugProcess();
    }

    @Override
    public XValueModifier getModifier() {
        if (modifier == null) modifier = new DBJdbcDebugValueModifier(this);
        return modifier;
    }
}
