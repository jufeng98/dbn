package com.dbn.common.listener;

import com.dbn.common.compatibility.Workaround;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dbn.common.dispose.Failsafe.guarded;

public class DBNFileEditorManagerListener implements FileEditorManagerListener {
    @Override
    public final void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file, @NotNull Pair<FileEditor[], FileEditorProvider[]> editors) {
    }

    //@Override
    @Workaround
    public final void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file, @NotNull List<FileEditorWithProvider> editorsWithProviders) {
    }

    @Override
    public final void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        guarded(this, l -> l.whenFileOpened(source, file));
    }

    @Override
    public final void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        guarded(this, l -> l.whenFileClosed(source, file));
    }

    @Override
    public final void selectionChanged(@NotNull FileEditorManagerEvent event) {
        guarded(this, l -> l.whenSelectionChanged(event));
    }

    public void whenFileOpened(FileEditorManager source, VirtualFile file) {}
    public void whenFileClosed(FileEditorManager source, VirtualFile file) {}
    public void whenSelectionChanged(FileEditorManagerEvent event) {}
}
