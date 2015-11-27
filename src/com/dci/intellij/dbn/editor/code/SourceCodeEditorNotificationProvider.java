package com.dci.intellij.dbn.editor.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.environment.options.listener.EnvironmentManagerListener;
import com.dci.intellij.dbn.common.thread.SimpleLaterInvocator;
import com.dci.intellij.dbn.common.util.EventUtil;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.editor.code.ui.SourceCodeEditorNotificationPanel;
import com.dci.intellij.dbn.editor.code.ui.SourceCodeLoadErrorNotificationPanel;
import com.dci.intellij.dbn.editor.code.ui.SourceCodeOutdatedNotificationPanel;
import com.dci.intellij.dbn.editor.code.ui.SourceCodeReadonlyNotificationPanel;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.DBContentVirtualFile;
import com.dci.intellij.dbn.vfs.DBEditableObjectVirtualFile;
import com.dci.intellij.dbn.vfs.DBSourceCodeVirtualFile;
import com.intellij.ide.FrameStateManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;

public class SourceCodeEditorNotificationProvider extends EditorNotifications.Provider<SourceCodeEditorNotificationPanel> {
    private static final Key<SourceCodeEditorNotificationPanel> KEY = Key.create("DBNavigator.SourceCodeEditorNotificationPanel");
    private Project project;

    public SourceCodeEditorNotificationProvider(final Project project, @NotNull FrameStateManager frameStateManager) {
        this.project = project;

        EventUtil.subscribe(project, project, SourceCodeManagerListener.TOPIC, sourceCodeManagerListener);
        EventUtil.subscribe(project, project, EnvironmentManagerListener.TOPIC, environmentManagerListener);
        EventUtil.subscribe(project, project, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener);
    }

    private SourceCodeManagerListener sourceCodeManagerListener = new SourceCodeManagerAdapter() {
        @Override
        public void sourceCodeLoaded(final DBSourceCodeVirtualFile sourceCodeFile, boolean isInitialLoad) {
            updateEditorNotification(sourceCodeFile);
        }
    };

    private EnvironmentManagerListener environmentManagerListener = new EnvironmentManagerListener() {
        @Override
        public void configurationChanged() {
            updateEditorNotification(null);
        }

        @Override
        public void editModeChanged(DBContentVirtualFile databaseContentFile) {
            if (databaseContentFile instanceof DBSourceCodeVirtualFile) {
                updateEditorNotification((DBSourceCodeVirtualFile) databaseContentFile);
            }
        }
    };

    private FileEditorManagerListener fileEditorManagerListener  =new FileEditorManagerAdapter() {
        @Override
        public void selectionChanged(@NotNull FileEditorManagerEvent event) {
            VirtualFile virtualFile = event.getNewFile();
            if (virtualFile instanceof DBEditableObjectVirtualFile) {

                DBEditableObjectVirtualFile databaseFile = (DBEditableObjectVirtualFile) virtualFile;
                for (DBContentVirtualFile contentFile : databaseFile.getContentFiles()) {
                    if (contentFile instanceof DBSourceCodeVirtualFile) {
                        DBSourceCodeVirtualFile sourceCodeFile = (DBSourceCodeVirtualFile) contentFile;
                        updateEditorNotification(sourceCodeFile);
                    }
                }
            }
        }
    };



    public void updateEditorNotification(@Nullable final DBSourceCodeVirtualFile sourceCodeFile) {
        new SimpleLaterInvocator() {
            @Override
            protected void execute() {
                if (!project.isDisposed()) {
                    EditorNotifications notifications = EditorNotifications.getInstance(project);
                    if (sourceCodeFile ==  null) {
                        notifications.updateAllNotifications();
                    } else {
                        notifications.updateNotifications(sourceCodeFile.getMainDatabaseFile());
                    }
                }
            }
        }.start();
    }

    @NotNull
    @Override
    public Key<SourceCodeEditorNotificationPanel> getKey() {
        return KEY;
    }

    @Nullable
    @Override
    public SourceCodeEditorNotificationPanel createNotificationPanel(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor) {
        if (virtualFile instanceof DBEditableObjectVirtualFile) {
            if (fileEditor instanceof SourceCodeEditor) {
                DBEditableObjectVirtualFile editableObjectFile = (DBEditableObjectVirtualFile) virtualFile;
                DBSchemaObject editableObject = editableObjectFile.getObject();
                SourceCodeEditor sourceCodeEditor = (SourceCodeEditor) fileEditor;
                DBSourceCodeVirtualFile sourceCodeFile = sourceCodeEditor.getVirtualFile();
                String sourceLoadError = sourceCodeFile.getSourceLoadError();
                if (StringUtil.isNotEmpty(sourceLoadError)) {
                    return createLoadErrorPanel(editableObject, sourceLoadError);
                } else if (sourceCodeFile.getEnvironmentType().isReadonlyCode()) {
                    return createReadonlyCodePanel(editableObject, sourceCodeFile, sourceCodeEditor);
                } else if (sourceCodeFile.isChangedInDatabase(false)) {
                    return createOutdatedCodePanel(editableObject, sourceCodeFile, sourceCodeEditor);
                }

            }
        }
        return null;
    }

    private static SourceCodeEditorNotificationPanel createLoadErrorPanel(final DBSchemaObject editableObject, String sourceLoadError) {
        return new SourceCodeLoadErrorNotificationPanel(editableObject, sourceLoadError);
    }

    private SourceCodeReadonlyNotificationPanel createReadonlyCodePanel(final DBSchemaObject editableObject, final DBSourceCodeVirtualFile sourceCodeFile, final SourceCodeEditor sourceCodeEditor) {
        return new SourceCodeReadonlyNotificationPanel(editableObject, sourceCodeEditor);
    }

    private SourceCodeEditorNotificationPanel createOutdatedCodePanel(final DBSchemaObject editableObject, final DBSourceCodeVirtualFile sourceCodeFile, final SourceCodeEditor sourceCodeEditor) {
        return new SourceCodeOutdatedNotificationPanel(editableObject, sourceCodeFile, sourceCodeEditor);
    }


}
