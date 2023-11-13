package com.dbn.menu.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.connection.ConnectionBundle;
import com.dbn.connection.ConnectionManager;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class PendingTransactionsOpenAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        if (connectionBundle.size() == 0) {
            connectionManager.promptMissingConnection();
            return;
        }

        DatabaseTransactionManager executionManager = DatabaseTransactionManager.getInstance(project);
        executionManager.showPendingTransactionsOverviewDialog(null);
    }
}
