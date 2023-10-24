package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.constant.PseudoConstant;
import com.dci.intellij.dbn.common.constant.PseudoConstantConverter;
import com.dci.intellij.dbn.connection.context.DatabaseContext;

import java.util.UUID;

public final class ConnectionId extends PseudoConstant<ConnectionId> {
    public static final ConnectionId VIRTUAL_ORACLE = get("virtual-oracle-connection");
    public static final ConnectionId VIRTUAL_MYSQL = get("virtual-mysql-connection");
    public static final ConnectionId VIRTUAL_POSTGRES = get("virtual-postgres-connection");
    public static final ConnectionId VIRTUAL_SQLITE = get("virtual-sqlite-connection");
    public static final ConnectionId VIRTUAL_ISO92_SQL = get("virtual-iso92-sql-connection");
    public static final ConnectionId UNKNOWN = get("unknown-connection");
    public static final ConnectionId DISPOSED = get("disposed-connection");
    public static final ConnectionId NULL = get("null-connection");

    private final int index;

    private ConnectionId(String id) {
        super(id);
        this.index = ConnectionIdIndex.next();
    }

    public static ConnectionId of(DatabaseContext context) {
        return context == null ? null :  context.getConnectionId();
    }

    public boolean isVirtual() {
        return
            this == VIRTUAL_ORACLE ||
            this == VIRTUAL_MYSQL ||
            this == VIRTUAL_POSTGRES ||
            this == VIRTUAL_SQLITE ||
            this == VIRTUAL_ISO92_SQL;
    }

    public int index() {
        return index;
    }

    public static ConnectionId get(String id) {
        return get(ConnectionId.class, id);
    }

    public static ConnectionId create() {
        return ConnectionId.get(UUID.randomUUID().toString());
    }

    public static class Converter extends PseudoConstantConverter<ConnectionId> {
        public Converter() {
            super(ConnectionId.class);
        }
    }

}
