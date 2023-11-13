package com.dbn.debugger.jdwp.process;

import com.dbn.connection.ConnectionHandler;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdwpLocalProcessStarter extends DBJdwpLocalProcessStarter {
    DBMethodJdwpLocalProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    @Override
    protected DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, DBJdwpTcpConfig tcpConfig) {
        return new DBMethodJdwpDebugProcess(session, debuggerSession, getConnection(), tcpConfig);
    }
}
