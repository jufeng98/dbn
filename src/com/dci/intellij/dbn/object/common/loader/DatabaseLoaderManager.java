package com.dci.intellij.dbn.object.common.loader;

import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.dispose.SafeDisposer;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.util.Documents;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionLoadListener;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.component.Components.projectService;

public class DatabaseLoaderManager extends ProjectComponentBase {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatabaseLoaderManager";

    private DatabaseLoaderQueue loaderQueue;

    private DatabaseLoaderManager(Project project) {
        super(project, COMPONENT_NAME);
        ProjectEvents.subscribe(project, this, ConnectionLoadListener.TOPIC, connectionLoadListener(project));
    }

    public static DatabaseLoaderManager getInstance(@NotNull Project project) {
        return projectService(project, DatabaseLoaderManager.class);
    }

    @NotNull
    private ConnectionLoadListener connectionLoadListener(Project project) {
        return connection -> Dispatch.run(() -> {
            checkDisposed();
            Failsafe.nn(project);
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
            VirtualFile[] openFiles = fileEditorManager.getOpenFiles();
            for (VirtualFile openFile : openFiles) {

                checkDisposed();
                ConnectionHandler activeConnection = contextManager.getConnection(openFile);
                if (activeConnection == connection) {
                    FileEditor[] fileEditors = fileEditorManager.getEditors(openFile);
                    for (FileEditor fileEditor : fileEditors) {

                        checkDisposed();
                        Editor editor = Editors.getEditor(fileEditor);
                        Documents.refreshEditorAnnotations(editor);
                    }

                }
            }
        });
    }

    @Override
    public void disposeInner() {
        SafeDisposer.dispose(loaderQueue);
        super.disposeInner();
    }
}
