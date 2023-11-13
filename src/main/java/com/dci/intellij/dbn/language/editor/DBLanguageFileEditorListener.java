package com.dci.intellij.dbn.language.editor;

import com.dci.intellij.dbn.common.listener.DBNFileEditorManagerListener;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.language.editor.ui.DBLanguageFileEditorToolbarForm;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.util.Files.isDbLanguageFile;
import static com.dci.intellij.dbn.common.util.Files.isLightVirtualFile;

public class DBLanguageFileEditorListener extends DBNFileEditorManagerListener {
    @Override
    public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (isNotValid(file)) return;
        if (!isDbLanguageFile(file)) return;
        if (!file.isInLocalFileSystem() && !isLightVirtualFile(file)) return;

        FileEditor fileEditor = source.getSelectedEditor(file);
        if (isNotValid(fileEditor)) return;

        DBLanguageFileEditorToolbarForm toolbarForm = new DBLanguageFileEditorToolbarForm(fileEditor, source.getProject(), file);
        Editors.addEditorToolbar(fileEditor, toolbarForm);
    }
}
