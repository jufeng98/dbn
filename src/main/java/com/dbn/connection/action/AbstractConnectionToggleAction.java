package com.dbn.connection.action;

import com.dbn.common.action.ToggleAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class AbstractConnectionToggleAction extends ToggleAction {
    private final ConnectionRef connection;

    public AbstractConnectionToggleAction(String text, @NotNull ConnectionHandler connection) {
        this(text, null, connection);

    }
    public AbstractConnectionToggleAction(String text, Icon icon, @NotNull ConnectionHandler connection) {
        this(text, null, icon, connection);
    }
    public AbstractConnectionToggleAction(String text, String description, Icon icon, @NotNull ConnectionHandler connection) {
        super(text, description, icon);
        this.connection = connection.ref();
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }
}
