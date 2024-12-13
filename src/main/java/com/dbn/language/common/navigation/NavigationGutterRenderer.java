package com.dbn.language.common.navigation;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NavigationGutterRenderer extends GutterIconRenderer {
    private final AnAction action;
    private final Alignment alignment;
    public NavigationGutterRenderer(@NotNull AnAction action, @NotNull Alignment alignment) {
        this.action = action;
        this.alignment = alignment;
    }

    @Override
    @NotNull
    public Icon getIcon() {
        //noinspection DataFlowIssue
        return action.getTemplatePresentation().getIcon();
    }

    @Override
    public boolean isNavigateAction() {
        return true;
    }

    @Override
    @Nullable
    public AnAction getClickAction() {
        return action;
    }

    @Override
    @Nullable
    public String getTooltipText() {
        //noinspection DialogTitleCapitalization
        return action.getTemplatePresentation().getText();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NavigationGutterRenderer renderer) {
            return action.equals(renderer.action);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return action.hashCode();
    }

    @NotNull
    @Override
    public Alignment getAlignment() {
        return alignment;
    }
}