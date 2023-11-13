package com.dci.intellij.dbn.diagnostics.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.action.AbstractConnectionAction;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import com.dci.intellij.dbn.object.common.DBObjectRecursiveLoaderVisitor;
import com.dci.intellij.dbn.object.common.list.DBObjectListContainer;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BulkLoadAllObjectsAction extends AbstractConnectionAction {
    public BulkLoadAllObjectsAction(ConnectionHandler connection) {
        super("Load All Objects", null, Icons.DATA_EDITOR_RELOAD_DATA, connection);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ConnectionHandler connection) {
        Progress.prompt(project, connection, true,
                "Loading data dictionary",
                "Loading all objects of " + connection.getName(),
                progress -> {
                    DBObjectListContainer objectListContainer = connection.getObjectBundle().getObjectLists();
                    objectListContainer.visit(DBObjectRecursiveLoaderVisitor.INSTANCE, false);
                });
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable ConnectionHandler connection) {
        presentation.setVisible(Diagnostics.isBulkActionsEnabled());
    }
}
