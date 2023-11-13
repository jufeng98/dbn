package com.dbn.common.editor;

import com.dbn.common.compatibility.LegacyEditorNotificationsProvider;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.util.Editors;
import com.dbn.vfs.file.DBContentVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class EditorNotificationProvider<T extends JComponent> extends LegacyEditorNotificationsProvider<T> {

    public EditorNotificationProvider() {}

    @Deprecated
    public EditorNotificationProvider(Project project) {
        super(project);
    }

    public void updateEditorNotification(@NotNull Project project, @Nullable DBContentVirtualFile databaseContentFile) {
        Dispatch.run(() -> {
            EditorNotifications notifications = Editors.getNotifications(project);
            if (databaseContentFile == null) {
                notifications.updateAllNotifications();
            } else {
                notifications.updateNotifications(databaseContentFile.getMainDatabaseFile());
            }
        });
    }

    @Override
    public boolean isWritable(@NotNull VirtualFile file) {
        return true;
    }

    @Override
    public boolean isNotWritable(@NotNull VirtualFile file) {
        return false;
    }
}
