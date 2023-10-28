package com.dci.intellij.dbn.data.editor.ui.text;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

class TextEditorRevertAction extends TextEditorAction {
    public TextEditorRevertAction() {
        super("Revert Changes", null, Icons.TEXT_CELL_EDIT_REVERT);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TextEditorPopupProviderForm form = getTextEditorForm(e);
        form.hidePopup();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        TextEditorPopupProviderForm form = getTextEditorForm(e);
        e.getPresentation().setEnabled(form != null && form.isChanged());
    }
}
