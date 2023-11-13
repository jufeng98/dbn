package com.dbn.connection.action;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.transaction.action.*;
import com.dbn.diagnostics.action.BulkLoadAllObjectsAction;
import com.dbn.diagnostics.action.MiscellaneousConnectionAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ConnectionActionGroup extends DefaultActionGroup {

    public ConnectionActionGroup(ConnectionHandler connection) {
        //add(new ConnectAction(connection));
        add(new TransactionCommitAction(connection));
        add(new TransactionRollbackAction(connection));
        add(new AutoCommitToggleAction(connection));
        add(new DatabaseLoggingToggleAction(connection));
        addSeparator();
        add(new SQLConsoleOpenAction(connection));
        add(new PendingTransactionsOpenAction(connection));
        addSeparator();
        add(new AutoConnectToggleAction(connection));
        add(new DatabaseConnectAction(connection));
        add(new DatabaseDisconnectAction(connection));
        add(new DatabaseConnectivityTestAction(connection));
        add(new BulkLoadAllObjectsAction(connection));
        add(new MiscellaneousConnectionAction(connection));
        addSeparator();
        add(new DatabaseInformationOpenAction(connection));
        add(new ConnectionSettingsOpenAction(connection));
    }
}
