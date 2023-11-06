package com.dci.intellij.dbn.debugger.jdwp.process;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

public class DBMethodJdwpLocalProcessStarter extends DBJdwpLocalProcessStarter {
    DBMethodJdwpLocalProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    @Override
    protected DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, String hostname, int tcpPort) {
        return new DBMethodJdwpDebugProcess(session,debuggerSession,getConnection(),hostname,tcpPort);
    }
}
