package com.dbn.common.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class ToggleAction
        extends com.intellij.openapi.actionSystem.ToggleAction
        implements BackgroundUpdatedAction, DumbAware {

    public ToggleAction() {
    }

    public ToggleAction(@Nullable String text) {
        super(text);
    }

    public ToggleAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
