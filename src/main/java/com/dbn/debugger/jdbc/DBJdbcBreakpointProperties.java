package com.dbn.debugger.jdbc;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.ConnectionRef;
import com.dbn.debugger.common.breakpoint.DBBreakpointProperties;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import org.jetbrains.annotations.Nullable;

public class DBJdbcBreakpointProperties extends XBreakpointProperties<DBJdbcBreakpointProperties> implements DBBreakpointProperties {
    @Attribute(value = "connection-id", converter = ConnectionId.Converter.class)
    private ConnectionId connectionId;
    private ConnectionRef connection;

    public DBJdbcBreakpointProperties() {
    }

    public DBJdbcBreakpointProperties(ConnectionHandler connection) {
        this.connection = ConnectionRef.of(connection);
        if (connection != null) {
            connectionId = connection.getConnectionId();
        }
    }

    @Override
    public ConnectionId getConnectionId() {
        return connectionId;
    }

    @Override
    @Nullable
    public ConnectionHandler getConnection() {
        if (connection == null && connectionId != null) {
            connection = ConnectionRef.of(connectionId);
        }
        return ConnectionRef.get(connection);
    }

    @Nullable
    @Override
    public DBJdbcBreakpointProperties getState() {
        return this;
    }

    @Override
    public void loadState(DBJdbcBreakpointProperties state) {
        connectionId = state.connectionId;
        connection = state.connection;
    }
}
