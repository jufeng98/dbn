package com.dbn.common.editor;

import com.dbn.common.dispose.StatefulDisposable;
import com.dbn.editor.EditorProviderId;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public interface BasicTextEditor<T extends VirtualFile> extends FileEditor, StatefulDisposable {
    @NotNull
    Editor getEditor();

    @NotNull
    T getVirtualFile();

    boolean canNavigateTo(@NotNull final Navigatable navigatable);

    void navigateTo(@NotNull final Navigatable navigatable);

    EditorProviderId getEditorProviderId();
}
