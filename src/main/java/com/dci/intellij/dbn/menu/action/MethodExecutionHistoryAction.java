package com.dci.intellij.dbn.menu.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.connection.ConnectionBundle;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.execution.method.MethodExecutionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MethodExecutionHistoryAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        if (connectionBundle.isEmpty()) {
            connectionManager.promptMissingConnection();
            return;
        }

        MethodExecutionManager executionManager = MethodExecutionManager.getInstance(project);
        executionManager.showExecutionHistoryDialog(null, true, false, null);
    }
}
