package com.dbn.common.editor;

import com.dbn.common.thread.Dispatch;
import com.dbn.common.util.Editors;
import com.dbn.vfs.file.DBContentVirtualFile;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessExtension;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.dbn.common.dispose.Failsafe.guarded;

public abstract class EditorNotificationProvider<T extends JComponent>
        extends EditorNotifications.Provider<T>
        implements NonProjectFileWritingAccessExtension, Disposable {

    public void updateEditorNotification(@NotNull Project project, @Nullable DBContentVirtualFile databaseContentFile) {
        Dispatch.run(() -> Editors.updateNotifications(project, DBEditableObjectVirtualFile.of(databaseContentFile)));
    }

    public final T createNotificationPanel(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor, @NotNull Project project) {
        return guarded(null, () -> createComponent(virtualFile, fileEditor, project));
    }

    public abstract T createComponent(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor, @NotNull Project project);


    @Override
    public boolean isWritable(@NotNull VirtualFile file) {
        return true;
    }

    @Override
    public boolean isNotWritable(@NotNull VirtualFile file) {
        return false;
    }

    @Override
    public void dispose() {

    }
}
