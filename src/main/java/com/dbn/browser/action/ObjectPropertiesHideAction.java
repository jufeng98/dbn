package com.dbn.browser.action;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.common.action.ProjectAction;
import com.dbn.common.icon.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ObjectPropertiesHideAction extends ProjectAction {
    public ObjectPropertiesHideAction() {
        super("Hide Properties", null, Icons.ACTION_CLOSE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        browserManager.showObjectProperties(false);
    }
}
