package com.dbn.connection.transaction.action;

import com.dbn.common.icon.Icons;
import com.dbn.connection.action.AbstractConnectionAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransactionRollbackAction extends AbstractConnectionAction {

    public TransactionRollbackAction(ConnectionHandler connection) {
        super("Rollback", "Rollback connection", Icons.CONNECTION_ROLLBACK, connection);

    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
        DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
        transactionManager.rollback(connection, null, false, false, null);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable ConnectionHandler connection) {
        boolean enabled = connection != null && connection.hasUncommittedChanges();
        boolean visible = connection != null && !connection.isAutoCommit();

        presentation.setEnabled(enabled);
        presentation.setVisible(visible);
    }
}
