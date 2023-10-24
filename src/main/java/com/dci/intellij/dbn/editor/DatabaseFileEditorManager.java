package com.dci.intellij.dbn.editor;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.CompoundIcons;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.file.VirtualFileInfo;
import com.dci.intellij.dbn.common.load.ProgressMonitor;
import com.dci.intellij.dbn.common.navigation.NavigationInstructions;
import com.dci.intellij.dbn.common.thread.*;
import com.dci.intellij.dbn.common.util.Editors;
import com.dci.intellij.dbn.common.util.Messages;
import com.dci.intellij.dbn.connection.ConnectionAction;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.ddl.DDLFileAttachmentManager;
import com.dci.intellij.dbn.ddl.options.DDLFileGeneralSettings;
import com.dci.intellij.dbn.ddl.options.DDLFileSettings;
import com.dci.intellij.dbn.editor.code.SourceCodeMainEditor;
import com.dci.intellij.dbn.editor.code.SourceCodeManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilter;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBConsole;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.property.DBObjectProperty;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.vfs.DatabaseFileManager;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.dci.intellij.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.dci.intellij.dbn.vfs.file.DBFileOpenHandle;
import com.dci.intellij.dbn.vfs.file.DBObjectVirtualFile;
import com.dci.intellij.dbn.vfs.file.status.DBFileStatus;
import com.dci.intellij.dbn.vfs.file.status.DBFileStatusListener;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.tabs.TabInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.dci.intellij.dbn.browser.DatabaseBrowserUtils.markSkipBrowserAutoscroll;
import static com.dci.intellij.dbn.browser.DatabaseBrowserUtils.unmarkSkipBrowserAutoscroll;
import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.common.dispose.Checks.allValid;
import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.message.MessageCallback.when;
import static com.dci.intellij.dbn.common.navigation.NavigationInstruction.*;
import static com.dci.intellij.dbn.common.util.Editors.getEditorTabInfos;
import static com.dci.intellij.dbn.editor.DatabaseFileEditorManager.COMPONENT_NAME;
import static com.dci.intellij.dbn.vfs.DatabaseFileSystem.isFileOpened;

@State(
        name = COMPONENT_NAME,
        storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatabaseFileEditorManager extends ProjectComponentBase {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatabaseFileEditorManager";

    public static DatabaseFileEditorManager getInstance(Project project) {
        return projectService(project, DatabaseFileEditorManager.class);
    }

    public DatabaseFileEditorManager(Project project) {
        super(project, COMPONENT_NAME);

        ProjectEvents.subscribe(project, this,
                DBFileStatusListener.TOPIC,
                createFileStatusListener());
    }

    @NotNull
    private DBFileStatusListener createFileStatusListener() {
        return (file, status, value) -> {
            if (status != DBFileStatus.MODIFIED) return;
            markEditorsModified(getProject(), file.getMainDatabaseFile(), value);
        };
    }

    private static void markEditorsModified(@NotNull Project project, @NotNull DBObjectVirtualFile file, boolean modified) {
        Dispatch.run(() -> {
            Collection<TabInfo> tabInfos = getEditorTabInfos(project, file);
            Icon icon = modified ? CompoundIcons.addModifiedOverlay(file.getIcon()) : file.getIcon();
            for (TabInfo tabInfo : tabInfos) {
                tabInfo.setIcon(icon);
            }
        });
    }

    public boolean isFileOpen(DBEditableObjectVirtualFile databaseFile) {
        FileEditorManager editorManager = FileEditorManager.getInstance(getProject());
        return editorManager.isFileOpen(databaseFile);
    }

    public void connectAndOpenEditor(@NotNull DBObject object, @Nullable EditorProviderId editorProviderId, boolean scrollBrowser, boolean focusEditor) {
        if (!isEditable(object)) return;

        ConnectionAction.invoke("opening the object editor", false, object, action -> {
            if (focusEditor) {
                Project project = object.getProject();
                Progress.prompt(project, object, true,
                        "Opening object editor",
                        "Opening editor for " + object.getQualifiedNameWithType(),
                        progress -> openEditor(object, editorProviderId, scrollBrowser, true));
            } else {
                Background.run(getProject(), () -> openEditor(object, editorProviderId, scrollBrowser, false));
            }
        });
    }

    public void openEditor(@NotNull DBObject object, @Nullable EditorProviderId editorProviderId, boolean scrollBrowser, boolean focusEditor) {
        if (!isEditable(object)) return;
        if (DBFileOpenHandle.isFileOpening(object)) return;

        NavigationInstructions editorInstructions = NavigationInstructions.create().with(OPEN).with(SCROLL, focusEditor).with(FOCUS, focusEditor);
        NavigationInstructions browserInstructions = NavigationInstructions.create().with(SCROLL, scrollBrowser);
        DBFileOpenHandle handle = DBFileOpenHandle.create(object).
                withEditorProviderId(editorProviderId).
                withEditorInstructions(editorInstructions).
                withBrowserInstructions(browserInstructions);

        try {
            handle.init();
            if (object.is(DBObjectProperty.SCHEMA_OBJECT)) {
                openSchemaObject(handle);

            } else {
                DBObject parentObject = object.getParentObject();
                if (parentObject.is(DBObjectProperty.SCHEMA_OBJECT)) {
                    openChildObject(handle);
                }
            }
        } finally {
            handle.release();
        }
    }

    private void openSchemaObject(@NotNull DBFileOpenHandle handle) {
        DBSchemaObject object = handle.getObject();
        object.makeEditorReady();

        DBEditableObjectVirtualFile databaseFile = getFileSystem().findOrCreateDatabaseFile(object);
        EditorProviderId editorProviderId = handle.getEditorProviderId();
        databaseFile.setSelectedEditorProviderId(editorProviderId);

        invokeFileOpen(handle, () -> {
            if (!allValid(object, databaseFile)) return;

            Project project = object.getProject();

            // open / reopen (select) the file
            if (isFileOpened(object) || promptFileOpen(databaseFile)) {
                boolean focusEditor = handle.getEditorInstructions().isFocus();

                Editors.openFileEditor(project, databaseFile, focusEditor);
                NavigationInstructions instructions = NavigationInstructions.create().
                        with(SCROLL).
                        with(FOCUS, focusEditor);
                Editors.selectEditor(project, null, databaseFile, editorProviderId, instructions);
            }
        });
    }

    private void openChildObject(DBFileOpenHandle handle) {
        DBObject object = handle.getObject();
        DBSchemaObject schemaObject = object.getParentObject();
        schemaObject.makeEditorReady();

        DBEditableObjectVirtualFile databaseFile = getFileSystem().findOrCreateDatabaseFile(schemaObject);

        Project project = schemaObject.getProject();
        SourceCodeManager sourceCodeManager = SourceCodeManager.getInstance(project);
        sourceCodeManager.ensureSourcesLoaded(schemaObject, false);

        invokeFileOpen(handle, () -> {
            if (isNotValid(schemaObject)) return;
            if (!isFileOpened(schemaObject) && !promptFileOpen(databaseFile)) return;

            // open / reopen (select) the file
            boolean focusEditor = handle.getEditorInstructions().isFocus();
            Editors.openFileEditor(project, databaseFile, focusEditor, fileEditors ->
                    focusEditor(handle, fileEditors, focusEditor, databaseFile));

        });
    }

    private void focusEditor(DBFileOpenHandle handle, FileEditor[] fileEditors, boolean focusEditor, DBEditableObjectVirtualFile databaseFile) {
        DBObject object = handle.getObject();
        Project project = object.getProject();
        for (FileEditor fileEditor : fileEditors) {
            if (fileEditor instanceof SourceCodeMainEditor) {
                SourceCodeMainEditor sourceCodeEditor = (SourceCodeMainEditor) fileEditor;
                NavigationInstructions instructions = NavigationInstructions.create().
                        with(SCROLL).
                        with(FOCUS, focusEditor);

                EditorProviderId editorProviderId = handle.getEditorProviderId();
                Editors.selectEditor(project, fileEditor, databaseFile, editorProviderId, instructions);
                sourceCodeEditor.navigateTo(object);
                break;
            }
        }
    }

    private static void invokeFileOpen(DBFileOpenHandle handle, Runnable opener) {
        if (ProgressMonitor.isProgressCancelled()) {
            handle.release();
        } else {
            DBObjectVirtualFile file = handle.getObject().getVirtualFile();
            try {
                markSkipBrowserAutoscroll(file);
                opener.run();
            } finally {
                unmarkSkipBrowserAutoscroll(file);
                handle.release();
            }
        }
    }

    private static boolean promptFileOpen(@NotNull DBEditableObjectVirtualFile databaseFile) {
        DBSchemaObject object = databaseFile.getObject();
        Project project = object.getProject();
        DBContentType contentType = object.getContentType();
        if (contentType == DBContentType.DATA) {
            DBDataset dataset = (DBDataset) object;
            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
            DatasetFilter filter = filterManager.getActiveFilter(dataset);

            if (filter == null) {
                DataEditorSettings settings = DataEditorSettings.getInstance(project);
                if (settings.getFilterSettings().isPromptFilterDialog()) {
                    int exitCode = filterManager.openFiltersDialog(dataset, true, false, settings.getFilterSettings().getDefaultFilterType());
                    return exitCode != DialogWrapper.CANCEL_EXIT_CODE;
                }
            }
        } else if (contentType.isOneOf(DBContentType.CODE, DBContentType.CODE_SPEC_AND_BODY)) {
            DDLFileGeneralSettings ddlFileSettings = DDLFileSettings.getInstance(project).getGeneralSettings();
            ConnectionHandler connection = object.getConnection();
            boolean ddlFileBinding = connection.getSettings().getDetailSettings().isEnableDdlFileBinding();
            if (!ddlFileBinding || !ddlFileSettings.isDdlFilesLookupEnabled()) return true;

            List<VirtualFile> attachedDDLFiles = databaseFile.getAttachedDDLFiles();
            if (attachedDDLFiles != null && !attachedDDLFiles.isEmpty()) return true;

            // do not prompt ddl file attachments during workspace restore
            if (ThreadInfo.current().is(ThreadProperty.WORKSPACE_RESTORE)) return true;

            DDLFileAttachmentManager fileAttachmentManager = DDLFileAttachmentManager.getInstance(project);
            DBObjectRef<DBSchemaObject> objectRef = DBObjectRef.of(object);
            List<VirtualFile> virtualFiles = fileAttachmentManager.lookupDetachedDDLFiles(objectRef);
            if (virtualFiles.isEmpty()) {
                if (ddlFileSettings.isDdlFilesCreationEnabled()) {
                    Messages.showQuestionDialog(
                            project, "No DDL file found",
                            "Could not find any DDL file for " + object.getQualifiedNameWithType() + ". Do you want to create one? \n" +
                                    "(You can disable this check in \"DDL File\" options)", Messages.OPTIONS_YES_NO, 0,
                            option -> when(option == 0, () -> fileAttachmentManager.createDDLFile(objectRef)));

                }
            } else {
                AtomicBoolean cancelled = new AtomicBoolean();
                List<VirtualFileInfo> fileInfos = VirtualFileInfo.fromFiles(virtualFiles, project);
                fileAttachmentManager.showFileAttachDialog(object, fileInfos, true, (dialog, exitCode) -> cancelled.set(exitCode == DialogWrapper.CANCEL_EXIT_CODE));
                return !cancelled.get();
            }
        }

        return true;
    }


    public void openDatabaseConsole(DBConsole console, boolean scrollBrowser, boolean focusEditor) {
        ConnectionHandler connection = console.getConnection();
        Project project = connection.getProject();

        NavigationInstructions editorInstructions = NavigationInstructions.create().with(OPEN).with(SCROLL, focusEditor).with(FOCUS, focusEditor);
        NavigationInstructions browserInstructions = NavigationInstructions.create().with(SCROLL, scrollBrowser);
        DBFileOpenHandle handle = DBFileOpenHandle.create(console).
                withEditorInstructions(editorInstructions).
                withBrowserInstructions(browserInstructions);

        invokeFileOpen(handle, () -> Editors.openFileEditor(project, console.getVirtualFile(), focusEditor));
    }

    public void closeEditor(DBSchemaObject object) {
        VirtualFile virtualFile = getFileSystem().findDatabaseFile(object);
        if (virtualFile == null) return;

        DatabaseFileManager databaseFileManager = DatabaseFileManager.getInstance(object.getProject());
        databaseFileManager.closeFile(virtualFile);
    }

    public void reopenEditor(DBSchemaObject object) {
        Project project = object.getProject();
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        VirtualFile virtualFile = getFileSystem().findOrCreateDatabaseFile(object);
        if (!editorManager.isFileOpen(virtualFile)) return;

        editorManager.closeFile(virtualFile);
        Editors.openFileEditor(project, virtualFile, false);
    }

    private boolean isEditable(DBObject object) {
        if (isNotValid(object)) return false;
        if (object.isEditable()) return true;

        DBObject parentObject = object.getParentObject();
        if (parentObject instanceof DBSchemaObject && parentObject != object) {
            return isEditable(parentObject);
        }

        return false;
    }


    private static DatabaseFileSystem getFileSystem() {
        return DatabaseFileSystem.getInstance();
    }
}
