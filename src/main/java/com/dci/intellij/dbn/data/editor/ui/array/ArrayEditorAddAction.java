package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

class ArrayEditorAddAction extends ArrayEditorAction {
    ArrayEditorAddAction() {
        super("Add Value", null, Icons.ARRAY_CELL_EDIT_ADD);
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_PLUS, UserInterface.ctrlDownMask()));
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_ADD, UserInterface.ctrlDownMask()));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ArrayEditorList editorList = getArrayEditorList(e);
        editorList.insertRow();
    }
}
