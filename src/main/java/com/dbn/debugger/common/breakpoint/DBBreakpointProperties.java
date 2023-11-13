package com.dbn.debugger.common.breakpoint;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import org.jetbrains.annotations.Nullable;

public interface DBBreakpointProperties {
    ConnectionId getConnectionId();

    @Nullable
    ConnectionHandler getConnection();
}
