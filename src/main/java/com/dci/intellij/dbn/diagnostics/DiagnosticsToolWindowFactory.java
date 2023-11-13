package com.dci.intellij.dbn.diagnostics;

import com.dci.intellij.dbn.common.icon.Icons;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class DiagnosticsToolWindowFactory implements ToolWindowFactory, DumbAware{
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindow.setTitle("DB Diagnostics");
        toolWindow.setStripeTitle("DB Diagnostics");
        toolWindow.setIcon(Icons.WINDOW_DATABASE_DIAGNOSTICS);
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.setAutoHide(false);
        toolWindow.setAvailable(false, null);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return false;
    }
}
