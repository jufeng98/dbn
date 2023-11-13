package com.dci.intellij.dbn.connection.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.object.status.ObjectStatusManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ObjectsStatusRefreshAction extends DumbAwareAction {

    private final ConnectionRef connection;

    public ObjectsStatusRefreshAction(ConnectionHandler connection) {
        super("Refresh objects status", "", Icons.ACTION_REFRESH);
        this.connection = connection.ref();
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = Lookups.ensureProject(e);
        ObjectStatusManager statusManager = ObjectStatusManager.getInstance(project);
        statusManager.refreshObjectsStatus(connection.get(), null);
    }
}
