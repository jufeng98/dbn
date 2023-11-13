package com.dbn.connection.transaction.ui;

import com.dbn.common.event.ProjectEvents;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionManager;
import com.dbn.connection.ConnectionType;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.dbn.connection.transaction.TransactionAction;
import com.dbn.connection.transaction.TransactionListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static com.dbn.connection.transaction.TransactionAction.*;

public class PendingTransactionsDialog extends DBNDialog<PendingTransactionsForm> {
    private final TransactionAction additionalOperation;

    public PendingTransactionsDialog(Project project, TransactionAction additionalOperation) {
        super(project, "Open transactions overview", true);
        this.additionalOperation = additionalOperation;
        setModal(false);
        setResizable(true);
        setDefaultSize(800, 600);
        init();
        ProjectEvents.subscribe(project, this, TransactionListener.TOPIC, transactionListener);
    }

    @NotNull
    @Override
    protected PendingTransactionsForm createForm() {
        return new PendingTransactionsForm(this);
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                commitAllAction,
                rollbackAllAction,
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    private final AbstractAction commitAllAction = new AbstractAction("Commit all") {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<TransactionAction> actions = actions(COMMIT, additionalOperation);
                executeActions(actions);
            } finally {
                doOKAction();
            }
        }

        @Override
        public boolean isEnabled() {
            return getForm().hasUncommittedChanges();
        }
    };

    private final AbstractAction rollbackAllAction = new AbstractAction("Rollback all") {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<TransactionAction> actions = actions(ROLLBACK, additionalOperation);
                executeActions(actions);
            } finally {
                doOKAction();
            }
        }

        @Override
        public boolean isEnabled() {
            return getForm().hasUncommittedChanges();
        }
    };

    private void executeActions(List<TransactionAction> actions) {
        DatabaseTransactionManager transactionManager = getTransactionManager();
        List<ConnectionHandler> connections = new ArrayList<>(getForm().getConnections());
        for (ConnectionHandler connection : connections) {
            List<DBNConnection> conns = connection.getConnections(ConnectionType.MAIN, ConnectionType.SESSION);
            for (DBNConnection conn : conns) {
                transactionManager.execute(connection, conn, actions, true, null);
            }

        }
    }

    private DatabaseTransactionManager getTransactionManager() {
        return DatabaseTransactionManager.getInstance(getProject());
    }

    private final TransactionListener transactionListener = new TransactionListener() {
        @Override
        public void afterAction(@NotNull ConnectionHandler connection, DBNConnection conn, TransactionAction action, boolean succeeded) {
            ConnectionManager connectionManager = ConnectionManager.getInstance(connection.getProject());
            if (!connectionManager.hasUncommittedChanges()) {
                Dispatch.run(() -> {
                    renameAction(getCancelAction(), "Close");
                    commitAllAction.setEnabled(false);
                    rollbackAllAction.setEnabled(false);
                });
            }
        }
    };
}
