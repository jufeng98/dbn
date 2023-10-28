package com.dci.intellij.dbn.editor.code.ui;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.ref.WeakRef;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.form.DBNToolbarForm;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.editor.code.SourceCodeEditor;
import com.dci.intellij.dbn.editor.code.SourceCodeManagerListener;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.util.ui.AsyncProcessIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SourceCodeEditorToolbarForm extends DBNToolbarForm {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel loadingDataPanel;
    private JLabel loadingLabel;
    private JPanel loadingIconPanel;

    private final WeakRef<SourceCodeEditor> sourceCodeEditor;

    public SourceCodeEditorToolbarForm(@NotNull SourceCodeEditor sourceCodeEditor) {
        super(sourceCodeEditor, sourceCodeEditor.getProject());
        this.mainPanel.setBorder(Borders.insetBorder(2));
        this.sourceCodeEditor = WeakRef.of(sourceCodeEditor);
        Editor editor = sourceCodeEditor.getEditor();

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.SourceEditor", "", true);
        this.actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);
        this.loadingIconPanel.add(new AsyncProcessIcon("Loading"), BorderLayout.CENTER);
        this.loadingDataPanel.setVisible(false);

        ProjectEvents.subscribe(ensureProject(), this, SourceCodeManagerListener.TOPIC, sourceCodeManagerListener());
        Disposer.register(sourceCodeEditor, this);
    }

    @NotNull
    private SourceCodeManagerListener sourceCodeManagerListener() {
        return new SourceCodeManagerListener() {
            @Override
            public void sourceCodeLoading(@NotNull DBSourceCodeVirtualFile sourceCodeFile) {
                DBSourceCodeVirtualFile virtualFile = getVirtualFile();
                if (virtualFile.equals(sourceCodeFile)) {
                    Dispatch.run(() -> loadingDataPanel.setVisible(true));
                }
            }

            @Override
            public void sourceCodeLoaded(@NotNull DBSourceCodeVirtualFile sourceCodeFile, boolean initialLoad) {
                DBSourceCodeVirtualFile virtualFile = getVirtualFile();
                if (virtualFile.equals(sourceCodeFile)) {
                    Dispatch.run(() -> loadingDataPanel.setVisible(false));
                }
            }
        };
    }

    @NotNull
    private DBSourceCodeVirtualFile getVirtualFile() {
        return getSourceCodeEditor().getVirtualFile();
    }

    @NotNull
    public SourceCodeEditor getSourceCodeEditor() {
        return sourceCodeEditor.ensure();
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public Object getData(@NotNull String dataId) {
        if (PlatformDataKeys.VIRTUAL_FILE.is(dataId)) return getSourceCodeEditor().getVirtualFile();
        if (PlatformDataKeys.FILE_EDITOR.is(dataId))  return getSourceCodeEditor();
        if (PlatformDataKeys.EDITOR.is(dataId)) return getSourceCodeEditor().getEditor();

        return null;
    }

}
