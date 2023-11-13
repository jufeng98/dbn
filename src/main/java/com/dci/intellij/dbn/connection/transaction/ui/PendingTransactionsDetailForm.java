package com.dci.intellij.dbn.connection.transaction.ui;

import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.form.DBNHeaderForm;
import com.dci.intellij.dbn.common.ui.misc.DBNScrollPane;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.transaction.DatabaseTransactionManager;
import com.dci.intellij.dbn.connection.transaction.TransactionAction;
import com.dci.intellij.dbn.connection.transaction.TransactionListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

import static com.dci.intellij.dbn.connection.transaction.TransactionAction.actions;


public class PendingTransactionsDetailForm extends DBNFormBase {
    private final PendingTransactionsTable pendingTransactionsTable;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JButton commitButton;
    private JButton rollbackButton;
    private JPanel transactionActionsPanel;
    private DBNScrollPane changesTableScrollPane;

    private final ConnectionRef connection;

    PendingTransactionsDetailForm(@NotNull DBNComponent parent, @NotNull ConnectionHandler connection, TransactionAction additionalOperation, boolean showActions) {
        super(parent);
        this.connection = connection.ref();

        DBNHeaderForm headerForm = new DBNHeaderForm(this, connection);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);

        PendingTransactionsTableModel transactionsTableModel = new PendingTransactionsTableModel(connection);
        pendingTransactionsTable = new PendingTransactionsTable(this, transactionsTableModel);
        changesTableScrollPane.setViewportView(pendingTransactionsTable);

        transactionActionsPanel.setVisible(showActions);
        if (showActions) {
            ActionListener actionListener = e -> {
                Project project = connection.getProject();
                DatabaseTransactionManager transactionManager = DatabaseTransactionManager.getInstance(project);
                List<DBNConnection> connections = pendingTransactionsTable.getSelectedConnections();
                Object source = e.getSource();

                for (DBNConnection conn : connections) {
                    if (source == commitButton) {
                        transactionManager.commit(connection, conn);
                    } else if (source == rollbackButton) {
                        List<TransactionAction> actions = actions(TransactionAction.ROLLBACK, additionalOperation);
                        transactionManager.rollback(connection, conn);
                    }
                }
            };

            commitButton.addActionListener(actionListener);
            commitButton.setIcon(Icons.CONNECTION_COMMIT);

            rollbackButton.addActionListener(actionListener);
            rollbackButton.setIcon(Icons.CONNECTION_ROLLBACK);

            ListSelectionModel selectionModel = pendingTransactionsTable.getSelectionModel();
            selectionModel.addListSelectionListener(e -> updateTransactionActions());

            if (transactionsTableModel.getRowCount()> 0) {
                pendingTransactionsTable.selectCell(0, 0);
            }
            updateTransactionActions();

        }
        ProjectEvents.subscribe(ensureProject(), this, TransactionListener.TOPIC, transactionListener);
    }

    private void updateTransactionActions() {
        List<DBNConnection> connections = pendingTransactionsTable.getSelectedConnections();
        boolean selectionAvailable = connections.size() > 0;
        commitButton.setEnabled(selectionAvailable);
        rollbackButton.setEnabled(selectionAvailable);
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @NotNull
    public List<DBNConnection> getConnections() {
        return pendingTransactionsTable.getModel().getTransactionalConnections();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    /********************************************************
     *                Transaction Listener                  *
     ********************************************************/
    private final TransactionListener transactionListener = new TransactionListener() {
        @Override
        public void afterAction(@NotNull ConnectionHandler connection, DBNConnection conn, TransactionAction action, boolean succeeded) {
            if (connection == getConnection() && succeeded) {
                refreshForm(connection);
            }
        }
    };

    private void refreshForm(ConnectionHandler connection) {
        Dispatch.run(() -> {
            checkDisposed();
            PendingTransactionsTableModel transactionsTableModel = new PendingTransactionsTableModel(connection);
            pendingTransactionsTable.setModel(transactionsTableModel);
            if (transactionsTableModel.getRowCount() > 0) {
                pendingTransactionsTable.selectCell(0, 0);
            }
            updateTransactionActions();
        });
    }
}
