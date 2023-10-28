package com.dci.intellij.dbn.editor.console.ui;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.ui.AutoCommitLabel;
import com.dci.intellij.dbn.common.ui.form.DBNToolbarForm;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.editor.console.SQLConsoleEditor;
import com.dci.intellij.dbn.vfs.file.DBConsoleVirtualFile;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SQLConsoleEditorToolbarForm extends DBNToolbarForm {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private AutoCommitLabel autoCommitLabel;

    public SQLConsoleEditorToolbarForm(Project project, SQLConsoleEditor fileEditor) {
        super(fileEditor, project);
        this.mainPanel.setBorder(Borders.insetBorder(2));

        ActionToolbar actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.FileEditor", "", true);
        this.actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);

        DBConsoleVirtualFile file = fileEditor.getVirtualFile();
        ConnectionHandler connection = file.getConnection();
        DatabaseSession session = file.getSession();
        this.autoCommitLabel.init(project, file, connection, session);
        Disposer.register(this, autoCommitLabel);
    }

    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        SQLConsoleEditor fileEditor = ensureParentComponent();
        if (PlatformDataKeys.VIRTUAL_FILE.is(dataId)) return fileEditor.getVirtualFile();
        if (PlatformDataKeys.FILE_EDITOR.is(dataId))  return fileEditor;
        if (PlatformDataKeys.EDITOR.is(dataId)) return fileEditor.getEditor();

        return null;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }
}
