package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionHandlerRef;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class ObjectsStatusRefreshAction extends DumbAwareAction {

    private final ConnectionHandlerRef connection;

    public ObjectsStatusRefreshAction(ConnectionHandler connection) {
        super("Refresh objects status");
        this.connection = connection.ref();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        connection.ensure().getObjectBundle().refreshObjectsStatus(null);
    }
}
