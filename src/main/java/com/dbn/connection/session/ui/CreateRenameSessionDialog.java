package com.dbn.connection.session.ui;

import com.dbn.common.ref.WeakRef;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.connection.session.DatabaseSession;
import com.dbn.connection.session.DatabaseSessionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CreateRenameSessionDialog extends DBNDialog<CreateRenameSessionForm> {
    private final ConnectionRef connection;
    private WeakRef<DatabaseSession> session;

    public CreateRenameSessionDialog(ConnectionHandler connection, @Nullable DatabaseSession session) {
        super(connection.getProject(), session == null ? "Create session" : "Rename session", true);
        this.connection = connection.ref();
        this.session = WeakRef.of(session);
        renameAction(getOKAction(), session == null ? "Create" : "Rename");
        setModal(true);
        init();
    }

    @NotNull
    @Override
    protected CreateRenameSessionForm createForm() {
        ConnectionHandler connection = this.connection.ensure();
        return new CreateRenameSessionForm(this, connection, getSession());
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
            getOKAction(),
            getCancelAction()
        };
    }

    @Override
    protected void doOKAction() {
        CreateRenameSessionForm component = getForm();
        DatabaseSessionManager databaseSessionManager = DatabaseSessionManager.getInstance(getProject());
        if (session == null) {
            DatabaseSession session = databaseSessionManager.createSession(
                    component.getConnection(),
                    component.getSessionName());
            this.session = WeakRef.of(session);
            component.setSession(session);

        } else {
            databaseSessionManager.renameSession(getSession(), component.getSessionName());
        }
        super.doOKAction();
    }

    public DatabaseSession getSession() {
        return WeakRef.get(session);
    }

    @Override
    @NotNull
    public Action getOKAction() {
        return super.getOKAction();
    }
}
