package com.dbn.menu.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.connection.ConnectionBundle;
import com.dbn.connection.ConnectionManager;
import com.dbn.execution.method.MethodExecutionManager;
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
        executionManager.showExecutionHistoryDialog(null, true, false, false, null);
    }
}
