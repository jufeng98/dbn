package com.dbn.debugger.jdwp.process;

import com.dbn.connection.ConnectionHandler;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

public class DBStatementJdwpCloudProcessStarter extends DBJdwpCloudProcessStarter{
    DBStatementJdwpCloudProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    @Override
    protected DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, DBJdwpTcpConfig tcpConfig) {
        return new DBStatementJdwpDebugProcess(session,debuggerSession,getConnection(), tcpConfig);
    }
}
