package com.dci.intellij.dbn.data.editor.ui.text;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.editor.ui.TextFieldWithPopup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class TextEditorDeleteAction extends TextEditorAction {
    public TextEditorDeleteAction() {
        super("Delete Content", null, Icons.TEXT_CELL_EDIT_DELETE);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TextEditorPopupProviderForm form = getTextEditorForm(e);
        JTextField textField = form.getTextField();
        TextFieldWithPopup editorComponent = form.getEditorComponent();
        editorComponent.getUserValueHolder().updateUserValue(null, false);
        editorComponent.setEditable(true);
        textField.setText("");
        form.hidePopup();
    }
}
