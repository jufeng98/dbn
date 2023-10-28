package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.action.BasicAction;
import com.dci.intellij.dbn.common.action.DataKeys;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class ArrayEditorAction extends BasicAction {
    public ArrayEditorAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    protected ArrayEditorPopupProviderForm getArrayEditorForm(@NotNull AnActionEvent e) {
        return e.getData(DataKeys.ARRAY_EDITOR_POPUP_PROVIDER_FORM);
    }

    protected ArrayEditorList getArrayEditorList(@NotNull AnActionEvent e) {
        return getArrayEditorForm(e).getEditorList();
    }
}
