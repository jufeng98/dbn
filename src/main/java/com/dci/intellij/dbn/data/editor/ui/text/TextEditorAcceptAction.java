package com.dci.intellij.dbn.data.editor.ui.text;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.data.editor.ui.TextFieldWithPopup;
import com.dci.intellij.dbn.data.editor.ui.UserValueHolder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class TextEditorAcceptAction extends TextEditorAction {
    public TextEditorAcceptAction() {
        super("Accept Changes", null, Icons.TEXT_CELL_EDIT_ACCEPT);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TextEditorPopupProviderForm form = getTextEditorForm(e);

        String text = form.getText().trim();
        TextFieldWithPopup<?> editorComponent = form.getEditorComponent();
        UserValueHolder userValueHolder = editorComponent.getUserValueHolder();
        userValueHolder.updateUserValue(text, false);

        if (userValueHolder.getUserValue() instanceof String) {
            JTextField textField = form.getTextField();
            editorComponent.setEditable(text.indexOf('\n') == -1);

            textField.setText(text);
        }
        form.hidePopup();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        TextEditorPopupProviderForm form = getTextEditorForm(e);
        e.getPresentation().setEnabled(form != null && form.isChanged());
    }
}
