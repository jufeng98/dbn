package com.dbn.debugger.common.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.intellij.xdebugger.XDebugProcessStarter;

public abstract class DBDebugProcessStarter extends XDebugProcessStarter {
    private final ConnectionRef connection;

    public DBDebugProcessStarter(ConnectionHandler connection) {
        this.connection = ConnectionRef.of(connection);
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }
}
