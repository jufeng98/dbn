package com.dbn.common.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Failsafe.guarded;

public abstract class ProjectActionGroup extends DefaultActionGroup implements BackgroundUpdatedAction, DumbAware {

    public ProjectActionGroup() {
        setPopup(true);
    }

    public DataProvider getDataProvider(AnActionEvent e) {
        return null;
    }

    @Override
    @NotNull
    public final AnAction[] getChildren(AnActionEvent e) {
        if (e == null) return AnAction.EMPTY_ARRAY;
        return guarded(AnAction.EMPTY_ARRAY, this, a -> a.loadChildren(e));
    }

    @NotNull
    protected abstract AnAction[] loadChildren(AnActionEvent e);

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
