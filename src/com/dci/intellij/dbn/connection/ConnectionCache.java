package com.dci.intellij.dbn.connection;

import com.dci.intellij.dbn.common.component.ApplicationComponent;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.project.Projects;
import com.dci.intellij.dbn.connection.config.ConnectionConfigListener;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionCache implements ApplicationComponent {
    private static final Map<ConnectionId, ConnectionHandler> cache = new ConcurrentHashMap<>();

    private static final ProcessCanceledException CANCELED_EXCEPTION = new ProcessCanceledException();

    public ConnectionCache() {
        Projects.projectOpened(project -> initializeCache(project));
        Projects.projectClosed(project -> releaseCache(project));

        ProjectEvents.subscribe(
                ConnectionConfigListener.TOPIC,
                ConnectionConfigListener.whenRemoved(id -> cache.remove(id)));
    }

    @Nullable
    public static ConnectionHandler resolveConnection(@Nullable ConnectionId connectionId) {
        if (connectionId != null) {
            try {
                return cache.computeIfAbsent(connectionId, id -> {
                    for (Project project : Projects.getOpenProjects()) {
                        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
                        ConnectionHandler connection = connectionManager.getConnection(id);
                        if (Failsafe.check(connection)) {
                            return connection;
                        }
                    }
                    throw CANCELED_EXCEPTION;
                });
            } catch (ProcessCanceledException ignore) {}
        }

        return null;
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "DBNavigator.ConnectionCache";
    }


    private static void initializeCache(@NotNull Project project) {
        if (!project.isDefault()) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(project);
            List<ConnectionHandler> connections = connectionManager.getConnections();
            for (ConnectionHandler connection : connections) {
                cache.put(connection.getConnectionId(), connection);
            }
        }
    }

    private static void releaseCache(@NotNull Project project) {
        if (!project.isDefault()) {
            Iterator<ConnectionId> connectionIds = cache.keySet().iterator();
            while (connectionIds.hasNext()) {
                ConnectionId connectionId = connectionIds.next();
                ConnectionHandler connection = cache.get(connectionId);
                if (connection.isDisposed() || connection.getProject() == project) {
                    connectionIds.remove();
                }
            }
        }
    }

    private static void refreshConnections(@NotNull Project project) {

    }
}
