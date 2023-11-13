package com.dbn.connection.action;

import com.dbn.connection.ConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionManager;
import com.dbn.connection.SessionId;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseConnectAction extends AbstractConnectionAction {
    DatabaseConnectAction(ConnectionHandler connection) {
        super("Connect", "Connect to " + connection.getName(), null, connection);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
        connection.getInstructions().setAllowAutoConnect(true);

        ConnectionAction.invoke("", true, connection,
                (action) -> ConnectionManager.testConnection(connection, null, SessionId.MAIN, false, true));
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable ConnectionHandler target) {
        presentation.setEnabled(target != null && !target.getConnectionStatus().isConnected());
    }

}
