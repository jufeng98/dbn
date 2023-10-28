package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

class ArrayEditorRemoveAction extends ArrayEditorAction {
    ArrayEditorRemoveAction() {
        super("Remove Value", null, Icons.ARRAY_CELL_EDIT_REMOVE);
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_MINUS, UserInterface.ctrlDownMask()));
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_SUBTRACT, UserInterface.ctrlDownMask()));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ArrayEditorList editorList = getArrayEditorList(e);
        editorList.removeRow();
    }
}
