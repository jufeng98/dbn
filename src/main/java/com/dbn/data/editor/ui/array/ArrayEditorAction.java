package com.dbn.data.editor.ui.array;

import com.dbn.common.action.BasicAction;
import com.dbn.common.action.DataKeys;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class ArrayEditorAction extends BasicAction {
    public ArrayEditorAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Nullable
    protected ArrayEditorPopupProviderForm getArrayEditorForm(@NotNull AnActionEvent e) {
        return e.getData(DataKeys.ARRAY_EDITOR_POPUP_PROVIDER_FORM);
    }

    @Nullable
    protected ArrayEditorList getArrayEditorList(@NotNull AnActionEvent e) {
        ArrayEditorPopupProviderForm form = getArrayEditorForm(e);
        return form == null ? null : form.getEditorList();
    }
}
