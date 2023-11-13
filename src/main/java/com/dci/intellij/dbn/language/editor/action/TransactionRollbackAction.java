package com.dci.intellij.dbn.language.editor.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TransactionRollbackAction extends TransactionEditorAction {
    public TransactionRollbackAction() {
        super("Rollback", "Rollback changes", Icons.CONNECTION_ROLLBACK);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ConnectionHandler connection = getConnection(e);
        if (connection != null) {
            DBNConnection conn = getTargetConnection(e);
            if (conn != null) {
                DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
                transactionManager.rollback(connection, conn, true, false, null);
            }
        }
    }
}
