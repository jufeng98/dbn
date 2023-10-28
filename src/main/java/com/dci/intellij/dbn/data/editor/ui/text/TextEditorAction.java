package com.dci.intellij.dbn.data.editor.ui.text;

import com.dci.intellij.dbn.common.action.BasicAction;
import com.dci.intellij.dbn.common.action.DataKeys;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class TextEditorAction extends BasicAction {
    public TextEditorAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    protected TextEditorPopupProviderForm getTextEditorForm(@NotNull AnActionEvent e) {
        return e.getData(DataKeys.TEXT_EDITOR_POPUP_PROVIDER_FORM);
    }
}
