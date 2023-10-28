package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

class ArrayEditorRevertAction extends ArrayEditorAction {
    ArrayEditorRevertAction() {
        super("Revert Changes", null, Icons.TEXT_CELL_EDIT_REVERT);
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_ESCAPE, 0));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ArrayEditorPopupProviderForm form = getArrayEditorForm(e);
        form.hidePopup();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        ArrayEditorPopupProviderForm form = getArrayEditorForm(e);
        e.getPresentation().setEnabled(form != null && form.isChanged());
    }
}
