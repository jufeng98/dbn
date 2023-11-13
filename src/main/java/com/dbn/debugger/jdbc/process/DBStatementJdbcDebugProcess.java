package com.dbn.debugger.jdbc.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.database.common.debug.DebuggerRuntimeInfo;
import com.dbn.debugger.jdbc.DBJdbcDebugProcess;
import com.dbn.debugger.common.config.DBStatementRunConfig;
import com.dbn.execution.ExecutionTarget;
import com.dbn.execution.statement.StatementExecutionInput;
import com.dbn.execution.statement.StatementExecutionManager;
import com.dbn.execution.statement.processor.StatementExecutionProcessor;
import com.dbn.object.DBMethod;
import com.dbn.object.common.DBSchemaObject;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class DBStatementJdbcDebugProcess extends DBJdbcDebugProcess<StatementExecutionInput> {
    DBStatementJdbcDebugProcess(@NotNull XDebugSession session, ConnectionHandler connection) {
        super(session, connection);
    }

    @Override
    protected void executeTarget() throws SQLException {
        StatementExecutionManager statementExecutionManager = StatementExecutionManager.getInstance(getProject());
        statementExecutionManager.debugExecute(getExecutionProcessor(), getTargetConnection());

    }

    @Override
    protected void registerDefaultBreakpoint() {
        DBStatementRunConfig runConfiguration = (DBStatementRunConfig) getSession().getRunProfile();
        if (runConfiguration == null) return;

        List<DBMethod> methods = runConfiguration.getMethods();
        if (methods.isEmpty()) return;

        getBreakpointHandler().registerDefaultBreakpoint(methods.get(0));
    }

    @Nullable
    @Override
    public VirtualFile getRuntimeInfoFile(DebuggerRuntimeInfo runtimeInfo) {
        DBSchemaObject schemaObject = getDatabaseObject(runtimeInfo);
        return schemaObject == null ?
            getExecutionProcessor().getVirtualFile() :
            schemaObject.getVirtualFile();
    }
    private StatementExecutionProcessor getExecutionProcessor() {
        return getExecutionInput().getExecutionProcessor();
    }

    @NotNull
    @Override
    public String getName() {
        return getExecutionProcessor().getName();
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return getExecutionProcessor().getIcon();
    }

    @Override
    public ExecutionTarget getExecutionTarget() {
        return ExecutionTarget.STATEMENT;
    }
}
