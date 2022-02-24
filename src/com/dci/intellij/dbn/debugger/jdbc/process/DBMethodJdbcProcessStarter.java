package com.dci.intellij.dbn.debugger.jdbc.process;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcessStarter;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdbcProcessStarter extends DBDebugProcessStarter {
    DBMethodJdbcProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    @NotNull
    @Override
    public XDebugProcess start(@NotNull XDebugSession session) {
        return new DBMethodJdbcDebugProcess(session, getConnection());
    }
}
