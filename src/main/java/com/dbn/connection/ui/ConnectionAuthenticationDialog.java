package com.dbn.connection.ui;

import com.dbn.common.database.AuthenticationInfo;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConnectionAuthenticationDialog extends DBNDialog<ConnectionAuthenticationForm> {
    private boolean rememberCredentials;
    private final WeakRef<AuthenticationInfo> authenticationInfo; // TODO dialog result - Disposable.nullify(...)
    private final ConnectionRef connection;

    @SuppressWarnings("removal")
    public ConnectionAuthenticationDialog(Project project, @Nullable ConnectionHandler connection, @NotNull AuthenticationInfo authenticationInfo) {
        super(project, "Enter password", true);
        this.authenticationInfo = WeakRef.of(authenticationInfo);
        setModal(true);
        setResizable(false);
        this.connection = ConnectionRef.of(connection);
        Action okAction = getOKAction();
        renameAction(okAction, "Connect");
        okAction.setEnabled(false);
        if (connection != null) {
            setDoNotAskOption(new DoNotAskOption() {
                @Override
                public boolean isToBeShown() {
                    return true;
                }

                @Override
                public void setToBeShown(boolean toBeShown, int exitCode) {
                    if (exitCode == OK_EXIT_CODE) {
                        rememberCredentials = !toBeShown;
                    }
                }

                @Override
                public boolean canBeHidden() {
                    return true;
                }

                @Override
                public boolean shouldSaveOptionsOnCancel() {
                    return false;
                }

                @NotNull
                @Override
                public String getDoNotShowMessage() {
                    return "Remember credentials";
                }
            });
        }
        init();
    }

    @NotNull
    @Override
    protected ConnectionAuthenticationForm createForm() {
        ConnectionHandler connection = ConnectionRef.get(this.connection);
        return new ConnectionAuthenticationForm(this, connection);
    }

    public boolean isRememberCredentials() {
        return rememberCredentials;
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return WeakRef.get(authenticationInfo);
    }

    public void updateConnectButton() {
        getOKAction().setEnabled(getAuthenticationInfo().isProvided());
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
        };
    }
    
    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
