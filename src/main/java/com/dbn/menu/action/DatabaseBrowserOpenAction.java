package com.dbn.menu.action;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.common.action.ProjectAction;
import com.dbn.common.dispose.Failsafe;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class DatabaseBrowserOpenAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = Failsafe.nn(toolWindowManager.getToolWindow(DatabaseBrowserManager.TOOL_WINDOW_ID));
        toolWindow.show(null);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();

        if (!project.isDefault()) {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow toolWindow = toolWindowManager.getToolWindow(DatabaseBrowserManager.TOOL_WINDOW_ID);
            presentation.setVisible(toolWindow != null && !toolWindow.isVisible());
        }

    }

}
