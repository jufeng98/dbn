package com.dci.intellij.dbn.data.editor.text.ui;

import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.common.ui.dialog.DBNDialog;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.data.editor.ui.DataEditorComponent;
import com.dci.intellij.dbn.data.editor.ui.UserValueHolder;
import com.dci.intellij.dbn.data.type.DBDataType;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TextEditorDialog extends DBNDialog<TextEditorForm> {
    private final DataEditorComponent textEditorAdapter;
    private TextEditorDialog(Project project, DataEditorComponent textEditorAdapter){
        super(project, getTitle(textEditorAdapter), true);
        this.textEditorAdapter = textEditorAdapter;
        renameAction(getCancelAction(), "Close");
        getOKAction().setEnabled(false);
        setModal(true);
        init();
    }

    @NotNull
    @Override
    protected TextEditorForm createForm() {
        UserValueHolder userValueHolder = textEditorAdapter.getUserValueHolder();
        return new TextEditorForm(this, documentListener, userValueHolder, textEditorAdapter);
    }

    @NotNull
    private static String getTitle(DataEditorComponent textEditorAdapter) {
        UserValueHolder userValueHolder = textEditorAdapter.getUserValueHolder();
        DBDataType dataType = userValueHolder.getDataType();
        String dataTypeName = dataType == null ? "OBJECT" : dataType.getName();
        DBObjectType objectType = userValueHolder.getObjectType();
        return "Edit " + dataTypeName.toUpperCase() + " content (" +objectType.getName().toLowerCase() + " " + userValueHolder.getName().toUpperCase() + ")";
    }

    public static void show(Project project, DataEditorComponent textEditorAdapter) {
        Dialogs.show(() -> new TextEditorDialog(project, textEditorAdapter));
    }

    @Override
    @NotNull
    protected final Action[] createActions() {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
                getHelpAction()
        };
    }

    @Override
    protected void doOKAction() {
        String text = getForm().getText();
        Progress.modal(getProject(), null, false,
                "Updating data",
                "Updating value from text editor",
                progress -> {
            UserValueHolder<String> userValueHolder = textEditorAdapter.getUserValueHolder();
            userValueHolder.updateUserValue(text, false);
            textEditorAdapter.afterUpdate();
        });
        super.doOKAction();
    }

    private final DocumentListener documentListener = new DocumentListener() {
        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
            renameAction(getCancelAction(), "Cancel");
            getOKAction().setEnabled(true);
        }
    };
}
