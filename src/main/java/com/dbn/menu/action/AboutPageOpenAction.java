package com.dbn.menu.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.plugin.about.ui.AboutComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class AboutPageOpenAction extends ProjectAction {

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        AboutComponent aboutComponent = new AboutComponent(project);
        aboutComponent.showPopup(project);
    }
}
