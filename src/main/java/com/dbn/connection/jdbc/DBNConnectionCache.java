package com.dbn.connection.jdbc;

import com.dbn.common.event.ProjectEvents;
import com.dbn.common.exception.Exceptions;
import com.dbn.common.notification.NotificationGroup;
import com.dbn.common.pool.ObjectCacheBase;
import com.dbn.common.thread.Background;
import com.dbn.connection.*;
import com.dbn.connection.*;
import com.intellij.openapi.project.Project;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;

import static com.dbn.common.notification.NotificationSupport.sendInfoNotification;

public class DBNConnectionCache extends ObjectCacheBase<SessionId, DBNConnection, SQLException> {
    private final ConnectionRef connection;

    public DBNConnectionCache(ConnectionHandler connection) {
        super(connection);
        this.connection = ConnectionRef.of(connection);
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Override
    public boolean check(DBNConnection conn) {
        return conn != null && !conn.isClosed() && conn.isValid();
    }

    @NotNull
    @Override
    @SneakyThrows
    protected DBNConnection create(SessionId sessionId) {
        ConnectionHandler connection = getConnection();
        Project project = connection.getProject();
        try {
            DBNConnection conn = ConnectionUtil.connect(connection, sessionId);
            sendInfoNotification(
                    project,
                    NotificationGroup.SESSION,
                    "Connected to database \"{0}\"",
                    connection.getConnectionName(conn));

            return conn;
        } finally {
            ProjectEvents.notify(project,
                    ConnectionStatusListener.TOPIC,
                    (listener) -> listener.statusChanged(connection.getConnectionId(), sessionId));
        }
    }

    @Override
    protected DBNConnection whenDropped(DBNConnection conn) {
        Background.run(null, () -> Resources.close(conn));
        return conn;
    }

    @Override
    protected DBNConnection whenReused(DBNConnection conn) {
        ConnectionHandler connection = getConnection();
        //connection.updateLastAccess(); TODO
        return conn;
    }

    @Override
    protected DBNConnection whenErrored(Throwable e) throws SQLException {
        throw Exceptions.toSqlException(e);
    }

    @Override
    protected DBNConnection whenNull() throws SQLException {
        throw new SQLRecoverableException("Failed to initialize connection");
    }
}
