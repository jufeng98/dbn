package com.dci.intellij.dbn.editor.console;

import com.dci.intellij.dbn.common.listener.DBNFileEditorManagerListener;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.editor.console.ui.SQLConsoleEditorToolbarForm;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.util.Files.isDbConsoleFile;

public class SQLConsoleEditorListener extends DBNFileEditorManagerListener {
    @Override
    public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (isNotValid(file)) return;
        if (!isDbConsoleFile(file)) return;

        SQLConsoleEditor fileEditor = (SQLConsoleEditor) source.getSelectedEditor(file);
        if (isNotValid(fileEditor)) return;

        Project project = source.getProject();
        SQLConsoleEditorToolbarForm toolbarForm = new SQLConsoleEditorToolbarForm(project, fileEditor);
        Editors.addEditorToolbar(fileEditor, toolbarForm);
    }
}
