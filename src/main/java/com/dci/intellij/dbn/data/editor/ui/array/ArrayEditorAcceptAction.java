package com.dci.intellij.dbn.data.editor.ui.array;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.editor.ui.UserValueHolder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

class ArrayEditorAcceptAction extends ArrayEditorAction {
    ArrayEditorAcceptAction() {
        super("Accept Changes", null, Icons.TEXT_CELL_EDIT_ACCEPT);
        //setShortcutSet(Keyboard.createShortcutSet(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ArrayEditorPopupProviderForm form = getArrayEditorForm(e);
        ArrayEditorList list = form.getEditorList();
        list.stopCellEditing();
        UserValueHolder userValueHolder = form.getEditorComponent().getUserValueHolder();
        userValueHolder.updateUserValue(list.getModel().getData(), false);

/*
        String text = editorTextArea.getText().trim();

        if (userValueHolder.getUserValue() instanceof String) {
            JTextField textField = getTextField();
            getEditorComponent().setEditable(text.indexOf('\n') == -1);

            textField.setText(text);
        }
*/
        form.hidePopup();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        ArrayEditorPopupProviderForm form = getArrayEditorForm(e);
        e.getPresentation().setEnabled(form != null && form.isChanged());
    }
}
