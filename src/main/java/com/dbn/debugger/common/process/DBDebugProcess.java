package com.dbn.debugger.common.process;

import com.dbn.common.property.PropertyHolder;
import com.dbn.common.ui.Presentable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dbn.debugger.DBDebugConsoleLogger;
import com.dbn.execution.ExecutionTarget;
import com.intellij.openapi.project.Project;

public interface DBDebugProcess extends Presentable, PropertyHolder<DBDebugProcessStatus> {
    ConnectionHandler getConnection();

    DBDebugConsoleLogger getConsole();

    Project getProject();

    DatabaseDebuggerInterface getDebuggerInterface();

    ExecutionTarget getExecutionTarget();
}
