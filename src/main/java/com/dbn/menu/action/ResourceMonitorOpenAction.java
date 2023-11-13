package com.dbn.menu.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.connection.transaction.DatabaseTransactionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ResourceMonitorOpenAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatabaseTransactionManager executionManager = DatabaseTransactionManager.getInstance(project);
        executionManager.showResourceMonitorDialog();
    }
}
