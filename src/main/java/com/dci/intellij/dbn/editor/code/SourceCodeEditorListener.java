package com.dci.intellij.dbn.editor.code;

import com.dci.intellij.dbn.common.listener.DBNFileEditorManagerListener;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.common.util.Files;
import com.dci.intellij.dbn.editor.code.ui.SourceCodeEditorToolbarForm;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.file.util.VirtualFiles.isLocalFileSystem;
import static com.dci.intellij.dbn.common.util.Files.isDbConsoleFile;

public class SourceCodeEditorListener extends DBNFileEditorManagerListener {
    @Override
    public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (isNotValid(file)) return;
        if (isDbConsoleFile(file)) return;
        if (isLocalFileSystem(file)) return;
        if (!Files.isDbEditableObjectFile(file)) return;

        FileEditor[] fileEditors = source.getEditors(file);
        for (FileEditor fileEditor : fileEditors) {
            if (fileEditor instanceof SourceCodeEditor) {
                SourceCodeEditor sourceCodeEditor = (SourceCodeEditor) fileEditor;
                SourceCodeEditorToolbarForm actionsPanel = new SourceCodeEditorToolbarForm(sourceCodeEditor);
                Editors.addEditorToolbar(fileEditor, actionsPanel);
            }
        }
    }

}
