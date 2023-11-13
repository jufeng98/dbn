package com.dbn.connection.transaction.ui;

import com.dbn.common.icon.Icons;
import com.dbn.common.ui.dialog.DialogWithTimeout;
import com.dbn.common.util.TimeUtil;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.jdbc.ResourceStatus;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.dbn.connection.transaction.TransactionAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static com.dbn.connection.transaction.TransactionAction.*;

public class IdleConnectionDialog extends DialogWithTimeout {
    private final IdleConnectionDialogForm idleConnectionDialogForm;
    private final ConnectionRef connection;
    private final DBNConnection conn;

    public IdleConnectionDialog(ConnectionHandler targetConnection, DBNConnection conn) {
        super(targetConnection.getProject(), "Idle connection", true, TimeUtil.getSeconds(5));
        this.connection = targetConnection.ref();
        this.conn = conn;
        idleConnectionDialogForm = new IdleConnectionDialogForm(this, targetConnection, conn, 5);
        setModal(false);
        init();
    }

    @Override
    protected JComponent createContentComponent() {
        return idleConnectionDialogForm.getComponent();
    }

    @Override
    public void doDefaultAction() {
        rollback();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Override
    protected void doOKAction() {
        try {
            conn.set(ResourceStatus.RESOLVING_TRANSACTION, false);
        } finally {
            super.doOKAction();
        }

    }

    @Override
    public void doCancelAction() {
        ping();
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                new CommitAction(),
                new RollbackAction(),
                new KeepAliveAction(),
                getHelpAction()
        };
    }

    private class CommitAction extends AbstractAction {
        CommitAction() {
            super("Commit", Icons.CONNECTION_COMMIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            commit();
        }
    }

    private class RollbackAction extends AbstractAction {
        RollbackAction() {
            super("Rollback", Icons.CONNECTION_ROLLBACK);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            rollback();
        }

    }
    private class KeepAliveAction extends AbstractAction {
        KeepAliveAction() {
            super("Keep Alive");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ping();
        }
    }

    private void commit() {
        try {
            List<TransactionAction> actions = actions(COMMIT, DISCONNECT_IDLE);
            DatabaseTransactionManager transactionManager = getTransactionManager();
            transactionManager.execute(getConnection(), conn, actions, true, null);
        } finally {
            doOKAction();
        }

    }

    private void rollback() {
        try {
            List<TransactionAction> actions = actions(ROLLBACK_IDLE, DISCONNECT_IDLE);
            DatabaseTransactionManager transactionManager = getTransactionManager();
            transactionManager.execute(getConnection(), conn, actions, true, null);
        } finally {
            doOKAction();
        }
    }

    private void ping() {
        try {
            List<TransactionAction> actions = actions(KEEP_ALIVE);
            DatabaseTransactionManager transactionManager = getTransactionManager();
            transactionManager.execute(getConnection(), conn, actions, true, null);
        } finally {
            doOKAction();
        }
    }

    private DatabaseTransactionManager getTransactionManager() {
        Project project = getProject();
        return DatabaseTransactionManager.getInstance(project);
    }
}
