package com.dbn.connection.info.ui;

import com.dbn.common.environment.EnvironmentType;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.connection.info.ConnectionInfo;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConnectionInfoDialog extends DBNDialog<ConnectionInfoForm> {
    private ConnectionRef connection;
    private ConnectionInfo connectionInfo;
    private String connectionName;
    private EnvironmentType environmentType;

    public ConnectionInfoDialog(@NotNull ConnectionHandler connection) {
        super(connection.getProject(), "Connection information", true);
        this.connection = connection.ref();
        renameAction(getCancelAction(), "Close");
        setResizable(false);
        setModal(true);
        init();
    }

    public ConnectionInfoDialog(Project project, ConnectionInfo connectionInfo, String connectionName, EnvironmentType environmentType) {
        super(project, "Connection information", true);
        this.connectionInfo = connectionInfo;
        this.connectionName = connectionName;
        this.environmentType = environmentType;
        renameAction(getCancelAction(), "Close");
        setResizable(false);
        setModal(true);
        init();
    }

    @NotNull
    @Override
    protected ConnectionInfoForm createForm() {
        if (connection != null) {
            ConnectionHandler connection = this.connection.ensure();
            return new ConnectionInfoForm(this, connection);
        } else {
            return new ConnectionInfoForm(this, connectionInfo, connectionName, environmentType);
        }
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
            getCancelAction()
        };
    }
}
