package com.dbn.ddl;

import com.dbn.common.compatibility.LegacyEditorNotificationsProvider;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.listener.DBNFileEditorManagerListener;
import com.dbn.common.util.Editors;
import com.dbn.ddl.options.DDLFileGeneralSettings;
import com.dbn.ddl.options.DDLFileSettings;
import com.dbn.ddl.options.listener.DDLFileSettingsChangeListener;
import com.dbn.ddl.ui.DDLMappedNotificationPanel;
import com.dbn.editor.ddl.DDLFileEditor;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dbn.common.dispose.Checks.allValid;
import static com.dbn.common.dispose.Checks.isNotValid;
import static com.dbn.common.util.Editors.isDdlFileEditor;
import static com.dbn.common.util.Files.isDbLanguageFile;
import static com.dbn.vfs.DatabaseFileSystem.isFileOpened;

public class DDLMappedNotificationProvider extends LegacyEditorNotificationsProvider<DDLMappedNotificationPanel> {
    private static final Key<DDLMappedNotificationPanel> KEY = Key.create("DBNavigator.DDLMappedNotificationPanel");

    public DDLMappedNotificationProvider() {
        ProjectEvents.subscribe(DDLFileSettingsChangeListener.TOPIC, ddlFileSettingsChangeListener());
        ProjectEvents.subscribe(DDLFileAttachmentManagerListener.TOPIC, ddlFileAttachmentManagerListener());
        ProjectEvents.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener());
    }

    @Deprecated
    public DDLMappedNotificationProvider(@NotNull Project project) {
        super(project);

        ProjectEvents.subscribe(project, this, DDLFileSettingsChangeListener.TOPIC, ddlFileSettingsChangeListener());
        ProjectEvents.subscribe(project, this, DDLFileAttachmentManagerListener.TOPIC, ddlFileAttachmentManagerListener());
        ProjectEvents.subscribe(project, this, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener());
    }


    @NotNull
    private static DDLFileAttachmentManagerListener ddlFileAttachmentManagerListener() {
        return new DDLFileAttachmentManagerListener() {
            @Override
            public void ddlFileDetached(Project project, VirtualFile virtualFile) {
                if (!project.isDisposed()) {
                    EditorNotifications notifications = Editors.getNotifications(project);;
                    notifications.updateNotifications(virtualFile);
                }
            }

            @Override
            public void ddlFileAttached(Project project, VirtualFile virtualFile) {
                if (!project.isDisposed()) {
                    EditorNotifications notifications = Editors.getNotifications(project);;
                    notifications.updateNotifications(virtualFile);
                }
            }
        };
    }

    @NotNull
    private static FileEditorManagerListener fileEditorManagerListener() {
        return new DBNFileEditorManagerListener() {
            @Override
            public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                updateDdlFileHeaders(source.getProject(), file);
            }

            @Override
            public void whenFileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                updateDdlFileHeaders(source.getProject(), file);
            }

            private void updateDdlFileHeaders(Project project, VirtualFile file) {
                if (allValid(project, file) && file instanceof DBEditableObjectVirtualFile) {
                    DBEditableObjectVirtualFile editableObjectFile = (DBEditableObjectVirtualFile) file;
                    DBObjectRef<DBSchemaObject> object = editableObjectFile.getObjectRef();
                    DDLFileAttachmentManager attachmentManager = DDLFileAttachmentManager.getInstance(project);
                    List<VirtualFile> attachedDDLFiles = attachmentManager.getAttachedDDLFiles(object);
                    if (attachedDDLFiles != null) {
                        EditorNotifications notifications = Editors.getNotifications(project);;
                        for (VirtualFile virtualFile : attachedDDLFiles) {
                            notifications.updateNotifications(virtualFile);
                        }
                    }
                }
            }
        };
    }

    @NotNull
    private static DDLFileSettingsChangeListener ddlFileSettingsChangeListener() {
        return (Project project) -> {
            EditorNotifications notifications = Editors.getNotifications(project);;
            notifications.updateAllNotifications();
        };
    }

    @NotNull
    @Override
    public Key<DDLMappedNotificationPanel> getKey() {
        return KEY;
    }

    @Override
    public DDLMappedNotificationPanel createComponent(@NotNull VirtualFile file, @NotNull FileEditor fileEditor, @NotNull Project project) {
        if (isNotValid(fileEditor)) return null;

        DDLFileSettings fileSettings = DDLFileSettings.getInstance(project);
        DDLFileGeneralSettings generalSettings = fileSettings.getGeneralSettings();
        if (!generalSettings.isDdlFilesSynchronizationEnabled()) return null;

        if (file instanceof DBEditableObjectVirtualFile) {
            if (!isDdlFileEditor(fileEditor)) return null;

            DBEditableObjectVirtualFile editableObjectFile = (DBEditableObjectVirtualFile) file;
            DBSchemaObject editableObject = editableObjectFile.getObject();
            DDLFileEditor ddlFileEditor = (DDLFileEditor) fileEditor;
            VirtualFile ddlVirtualFile = Failsafe.nn(ddlFileEditor.getVirtualFile());
            return createPanel(ddlVirtualFile, editableObject);
        } else {
            if (!isDbLanguageFile(file)) return null;

            DDLFileAttachmentManager attachmentManager = DDLFileAttachmentManager.getInstance(project);
            DBSchemaObject object = attachmentManager.getMappedObject(file);
            if (isNotValid(object)) return null;
            if (!isFileOpened(object)) return null;

            return createPanel(file, object);
        }
    }

    private DDLMappedNotificationPanel createPanel(@NotNull final VirtualFile virtualFile, final DBSchemaObject editableObject) {
        return new DDLMappedNotificationPanel(virtualFile, editableObject);
    }
}
