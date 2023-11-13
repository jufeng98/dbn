package com.dbn.connection;

import com.dbn.common.project.Projects;
import com.dbn.common.util.TimeUtil;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class ConnectionPoolCleaner extends TimerTask {
    public static final ConnectionPoolCleaner INSTANCE = new ConnectionPoolCleaner();


    @Override
    public void run() {
        for (Project project : Projects.getOpenProjects()) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(project);
            List<ConnectionHandler> connections = connectionManager.getConnections();
            for (ConnectionHandler connection : connections) {
                ConnectionPool connectionPool = connection.getConnectionPool();
                connectionPool.clean();
            }

        }
    }

    void start() {
        Timer poolCleaner = new Timer("DBN - Idle Connection Pool Cleaner");
        poolCleaner.schedule(INSTANCE, TimeUtil.Millis.ONE_MINUTE, TimeUtil.Millis.ONE_MINUTE);
    }

}


