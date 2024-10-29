package com.dbn.ddl;

import com.dbn.utils.VirtualFileUtils;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class MessageDialog extends DialogWrapper {
    private final Runnable okRunnable;
    private JPanel mainPanel;
    private JPanel textPanel;
    private final TextEditor textEditor;

    public MessageDialog(@Nullable Project project, String message,Runnable okRunnable) {
        super(project);
        this.okRunnable = okRunnable;

        setTitle("Info");
        setOKButtonText("关闭");
        setModal(false);

        init();

        textEditor = createTextEditorAndSetText(project, message.getBytes(StandardCharsets.UTF_8));
        textPanel.add(textEditor.getComponent(), BorderLayout.CENTER);
    }

    @Override
    protected void dispose() {
        super.dispose();
        Disposer.dispose(textEditor);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        okRunnable.run();
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{getOKAction()};
    }

    @Override
    protected @Nullable ActionListener createCancelAction() {
        return null;
    }

    private TextEditor createTextEditorAndSetText(Project project, byte[] txtBytes) {
        final TextEditor[] textEditors = new TextEditor[1];
        WriteAction.runAndWait(() -> {
            VirtualFile virtualFile = VirtualFileUtils.createVirtualSqlFileFromText(txtBytes);

            TextEditor textEditor = (TextEditor) TextEditorProvider.getInstance().createEditor(project, virtualFile);
            Editor editor = textEditor.getEditor();

            editor.getDocument().setReadOnly(true);

            textEditors[0] = textEditor;

        });
        return textEditors[0];
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }
}
