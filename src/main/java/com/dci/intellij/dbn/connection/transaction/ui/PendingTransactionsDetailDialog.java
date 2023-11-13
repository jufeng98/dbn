package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.dci.intellij.dbn.connection.transaction.TransactionAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static com.dci.intellij.dbn.connection.transaction.TransactionAction.*;

public class PendingTransactionsDetailDialog extends DBNDialog<PendingTransactionsDetailForm> {
    private final ConnectionRef connection;
    private final TransactionAction additionalOperation;
    private final boolean showActions;

    public PendingTransactionsDetailDialog(ConnectionHandler connection, TransactionAction additionalOperation, boolean showActions) {
        super(connection.getProject(), "Open transactions", true);
        this.connection = connection.ref();
        this.additionalOperation = additionalOperation;
        this.showActions = showActions;
        setModal(false);
        setResizable(true);
        init();
    }

    @NotNull
    @Override
    protected PendingTransactionsDetailForm createForm() {
        return new PendingTransactionsDetailForm(this, getConnection(), additionalOperation, showActions);
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                new CommitAction(),
                new RollbackAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    private class CommitAction extends AbstractAction {
        CommitAction() {
            super("Commit", Icons.CONNECTION_COMMIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<TransactionAction> actions = actions(COMMIT, additionalOperation);
                executeActions(actions);
            } finally {
                doOKAction();
            }
        }
    }

    private class RollbackAction extends AbstractAction {
        RollbackAction() {
            super("Rollback", Icons.CONNECTION_ROLLBACK);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<TransactionAction> actions = actions(ROLLBACK, additionalOperation);
                executeActions(actions);
            } finally {
                doOKAction();
            }
        }
    }

    protected void executeActions(List<TransactionAction>  actions) {
        ConnectionHandler connection = getConnection();
        List<DBNConnection> connections = getForm().getConnections();
        DatabaseTransactionManager transactionManager = getTransactionManager();
        for (DBNConnection conn : connections) {
            transactionManager.execute(connection, conn, actions, true, null);
        }
    }

    private DatabaseTransactionManager getTransactionManager() {
        Project project = getConnection().getProject();
        return DatabaseTransactionManager.getInstance(project);
    }
}
