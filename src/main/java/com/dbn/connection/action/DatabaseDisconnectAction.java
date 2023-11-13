package com.dbn.connection.action;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseDisconnectAction extends AbstractConnectionAction {
    DatabaseDisconnectAction(ConnectionHandler connection) {
        super("Disconnect", "Disconnect from " + connection.getName(), null, connection);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
        DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(connection.getProject());
        transactionManager.disconnect(connection);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable ConnectionHandler target) {
        presentation.setEnabled(target != null && target.getConnectionStatus().isConnected());
    }
}
