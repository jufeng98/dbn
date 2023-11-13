package com.dbn.debugger.jdwp;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.ConnectionRef;
import com.dbn.debugger.common.breakpoint.DBBreakpointProperties;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.java.debugger.breakpoints.properties.JavaBreakpointProperties;

public class DBJdwpBreakpointProperties extends JavaBreakpointProperties<DBJdwpBreakpointProperties> implements DBBreakpointProperties {
    @Attribute(value = "connection-id", converter = ConnectionId.Converter.class)
    private ConnectionId connectionId;
    private ConnectionRef connection;

    public DBJdwpBreakpointProperties() {
    }

    public DBJdwpBreakpointProperties(ConnectionHandler connection) {
        this.connection = ConnectionRef.of(connection);
        if (connection != null) {
            connectionId = connection.getConnectionId();
        }
    }

    @Override
    public ConnectionId getConnectionId() {
        return connectionId;
    }

    @Nullable
    @Override
    public ConnectionHandler getConnection() {
        if (connection == null && connectionId != null) {
            connection = ConnectionRef.of(connectionId);
        }
        return ConnectionRef.get(connection);
    }

    @Nullable
    @Override
    public DBJdwpBreakpointProperties getState() {
        return super.getState();
    }

    @Override
    public void loadState(@NotNull DBJdwpBreakpointProperties state) {
        super.loadState(state);
        connectionId = state.connectionId;
        connection = state.connection;
    }
}
