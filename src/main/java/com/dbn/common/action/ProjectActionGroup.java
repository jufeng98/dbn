package com.dbn.common.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectActionGroup extends DefaultActionGroup implements BackgroundUpdatedAction, DumbAware {

    public ProjectActionGroup() {
        setPopup(true);
    }

    public DataProvider getDataProvider(AnActionEvent e) {
        return null;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
