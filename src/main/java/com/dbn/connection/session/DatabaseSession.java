package com.dbn.connection.session;

import com.dbn.common.icon.Icons;
import com.dbn.common.index.Identifiable;
import com.dbn.common.ui.Presentable;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.connection.ConnectionType;
import com.dbn.connection.SessionId;
import com.dbn.connection.jdbc.DBNConnection;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Failsafe.guarded;

@Getter
@Setter
public class DatabaseSession implements Comparable<DatabaseSession>, Presentable, Identifiable<SessionId> {
    private final ConnectionRef connection;
    private final ConnectionType connectionType;
    private final SessionId id;
    private String name;

    public DatabaseSession(SessionId id, String name, ConnectionType connectionType, ConnectionHandler connection) {
        this.id = id == null ? SessionId.create() : id;
        this.name = name;
        this.connectionType = connectionType;
        this.connection = connection.ref();
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return guarded(Icons.SESSION_CUSTOM, this, s -> {
            if (s.isPool()) {
                return Icons.SESSION_POOL;
            } else {
                DBNConnection connection = s.getConnection().getConnectionPool().getSessionConnection(s.getId());
                if (connection == null || !connection.isValid()) {
                    return s.isMain() ?  Icons.SESSION_MAIN :
                           s.isDebug() ? Icons.SESSION_DEBUG :
                                            Icons.SESSION_CUSTOM;
                } else if (connection.hasDataChanges()) {
                    return s.isMain() ? Icons.SESSION_MAIN_TRANSACTIONAL :
                           s.isDebug() ? Icons.SESSION_DEBUG_TRANSACTIONAL :
                                        Icons.SESSION_CUSTOM_TRANSACTIONAL;
                } else {
                    return s.isMain() ? Icons.SESSION_MAIN :
                           s.isDebug() ? Icons.SESSION_DEBUG :
                                        Icons.SESSION_CUSTOM;
                }
            }
        });
    }

    public boolean isMain() {
        return id == SessionId.MAIN;
    }

    public boolean isDebug() {
        return id == SessionId.DEBUG || id == SessionId.DEBUGGER;
    }

    public boolean isPool() {
        return id == SessionId.POOL;
    }

    public boolean isCustom() {
        return !isMain() && !isPool() && !isDebug();
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Override
    public int compareTo(@NotNull DatabaseSession o) {
        if (id == SessionId.MAIN) return -1;
        if (id == SessionId.POOL) {
            return o.id == SessionId.MAIN ? 1 : -1;
        }
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
