package com.dbn.database.common.execution;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.debugger.DBDebuggerType;
import com.dbn.execution.method.MethodExecutionInput;
import com.dbn.object.DBMethod;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public interface MethodExecutionProcessor {
    void execute(MethodExecutionInput executionInput, DBDebuggerType debuggerType) throws SQLException;

    void execute(MethodExecutionInput executionInput, DBNConnection connection, DBDebuggerType debuggerType) throws SQLException;

    @NotNull
    DBMethod getMethod();
}
