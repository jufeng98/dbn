package com.dci.intellij.dbn.language.editor.ui;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.ui.AutoCommitLabel;
import com.dci.intellij.dbn.common.ui.form.DBNToolbarForm;
import com.dci.intellij.dbn.common.ui.util.Borders;
import com.dci.intellij.dbn.common.util.Actions;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextListener;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Objects;

public class DBLanguageFileEditorToolbarForm extends DBNToolbarForm {
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private AutoCommitLabel autoCommitLabel;

    private final ActionToolbar actionToolbar;

    public DBLanguageFileEditorToolbarForm(FileEditor fileEditor, Project project, VirtualFile file) {
        super(fileEditor, project);
        this.mainPanel.setBorder(Borders.insetBorder(2));

        this.actionToolbar = Actions.createActionToolbar(actionsPanel, "DBNavigator.ActionGroup.FileEditor", "", true);
        this.actionsPanel.add(actionToolbar.getComponent(), BorderLayout.CENTER);
        this.actionToolbar.getComponent().addComponentListener(createResizeListener());

        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        ConnectionHandler connection = contextManager.getConnection(file);
        DatabaseSession databaseSession = contextManager.getDatabaseSession(file);

        this.autoCommitLabel.init(project, file, connection, databaseSession);
        Disposer.register(this, autoCommitLabel);

        ProjectEvents.subscribe(project, this, FileConnectionContextListener.TOPIC, createConnectionChangeListener(file));
    }

    @NotNull
    private ComponentListener createResizeListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = actionsPanel.getSize();
                Dimension preferredSize = actionToolbar.getComponent().getPreferredSize();
                if (size.getWidth() == preferredSize.getWidth()) return;
                actionsPanel.setSize(preferredSize);
            }
        };
    }

    @NotNull
    private FileConnectionContextListener createConnectionChangeListener(VirtualFile file) {
        return new FileConnectionContextListener() {
            @Override
            public void connectionChanged(Project project, VirtualFile virtualFile, ConnectionHandler connection) {
                if (Objects.equals(file, virtualFile)) {
                    actionToolbar.updateActionsImmediately();
                }
            }
        };
    }

    @Nullable
    @Override
    public Object getData(@NotNull String dataId) {
        FileEditor fileEditor = ensureParentComponent();
        if (fileEditor instanceof TextEditor) {
            TextEditor textEditor = (TextEditor) fileEditor;
            if (PlatformDataKeys.VIRTUAL_FILE.is(dataId)) return textEditor.getFile();
            if (PlatformDataKeys.FILE_EDITOR.is(dataId))  return textEditor;
            if (PlatformDataKeys.EDITOR.is(dataId)) return textEditor.getEditor();
        }

        return null;
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public AutoCommitLabel getAutoCommitLabel() {
        return autoCommitLabel;
    }
}
