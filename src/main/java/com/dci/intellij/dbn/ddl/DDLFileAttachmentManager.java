package com.dci.intellij.dbn.ddl;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.file.VirtualFileInfo;
import com.dci.intellij.dbn.common.file.util.FileSearchRequest;
import com.dci.intellij.dbn.common.file.util.VirtualFiles;
import com.dci.intellij.dbn.common.thread.Background;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.thread.Progress;
import com.dci.intellij.dbn.common.ui.util.Lists;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.common.util.Dialogs.DialogCallback;
import com.dci.intellij.dbn.common.util.Documents;
import com.dci.intellij.dbn.common.util.Messages;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.config.ConnectionConfigListener;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.ddl.options.DDLFileSettings;
import com.dci.intellij.dbn.ddl.ui.AttachDDLFileDialog;
import com.dci.intellij.dbn.ddl.ui.DDLFileNameListCellRenderer;
import com.dci.intellij.dbn.ddl.ui.DetachDDLFileDialog;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.DatabaseFileEditorManager;
import com.dci.intellij.dbn.editor.code.SourceCodeEditor;
import com.dci.intellij.dbn.editor.code.SourceCodeManagerListener;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.dci.intellij.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.SelectFromListDialog;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;
import static com.dci.intellij.dbn.common.message.MessageCallback.when;
import static com.dci.intellij.dbn.common.options.setting.Settings.newElement;
import static com.dci.intellij.dbn.common.options.setting.Settings.stringAttribute;
import static com.dci.intellij.dbn.common.util.Lists.anyMatch;
import static com.dci.intellij.dbn.common.util.Messages.options;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.dci.intellij.dbn.vfs.DatabaseFileSystem.isFileOpened;

@State(
    name = DDLFileAttachmentManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DDLFileAttachmentManager extends ProjectComponentBase implements PersistentState {

    public static final String COMPONENT_NAME = "DBNavigator.Project.DDLFileAttachmentManager";

    private final Map<String, DBObjectRef<DBSchemaObject>> mappings = new ConcurrentHashMap<>();
    private DDLFileAttachmentManager(@NotNull Project project) {
        super(project, COMPONENT_NAME);

        //VirtualFileManager.getInstance().addVirtualFileListener(virtualFileListener);
        ProjectEvents.subscribe(project, this, VirtualFileManager.VFS_CHANGES, bulkFileListener());
        ProjectEvents.subscribe(project, this, SourceCodeManagerListener.TOPIC, sourceCodeManagerListener());
        ProjectEvents.subscribe(project, this, ConnectionConfigListener.TOPIC, connectionConfigListener());
    }

    public static DDLFileAttachmentManager getInstance(@NotNull Project project) {
        return projectService(project, DDLFileAttachmentManager.class);
    }

    @NotNull
    private BulkFileListener bulkFileListener() {
        return new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    VirtualFile file = event.getFile();
                    if (file != null) {
                        if (event instanceof VFileDeleteEvent) {
                            processFileDeletedEvent(file);
                        }
                    }
                }
            }
        };
    }

    @NotNull
    private SourceCodeManagerListener sourceCodeManagerListener() {
        return new SourceCodeManagerListener() {
            @Override
            public void sourceCodeLoaded(@NotNull DBSourceCodeVirtualFile sourceCodeFile, boolean initialLoad) {
                if (!initialLoad && DatabaseFileSystem.isFileOpened(sourceCodeFile.getObject())) {
                    updateDDLFiles(sourceCodeFile.getMainDatabaseFile());
                }
            }

            @Override
            public void sourceCodeSaved(@NotNull DBSourceCodeVirtualFile sourceCodeFile, @Nullable SourceCodeEditor fileEditor) {
                updateDDLFiles(sourceCodeFile.getMainDatabaseFile());
            }
        };
    }

    @NotNull
    private ConnectionConfigListener connectionConfigListener() {
        return new ConnectionConfigListener() {
            @Override
            public void connectionRemoved(ConnectionId connectionId) {
                mappings
                        .entrySet()
                        .stream()
                        .filter(m -> Objects.equals(m.getValue().getConnectionId(), connectionId))
                        .map(m -> m.getKey())
                        .forEach(k -> mappings.remove(k));
            }
        };
    }

    @Nullable
    public List<VirtualFile> getAttachedDDLFiles(DBObjectRef<DBSchemaObject> objectRef) {
        List<String> fileUrls = getAttachedFileUrls(objectRef);
        List<VirtualFile> virtualFiles = null;

        if (!fileUrls.isEmpty()) {
            VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
            for (String fileUrl : fileUrls) {
                VirtualFile virtualFile = virtualFileManager.findFileByUrl(fileUrl);
                if (isNotValid(virtualFile)) {
                    mappings.remove(fileUrl);
                } else {
                    if (virtualFiles == null) virtualFiles = new ArrayList<>();
                    virtualFiles.add(virtualFile);
                }
            }
        }
        checkInvalidAttachedFiles(virtualFiles, objectRef);
        return virtualFiles;
    }

    @Nullable
    public DBSchemaObject getMappedObject(@NotNull VirtualFile ddlFile) {
        return DBObjectRef.get(getMappedObjectRef(ddlFile));
    }

    @Nullable
    public DBObjectRef<DBSchemaObject> getMappedObjectRef(@NotNull VirtualFile ddlFile) {
        return mappings.get(ddlFile.getUrl());
    }

    public ConnectionHandler getMappedConnection(VirtualFile ddlFile) {
        DBObjectRef<DBSchemaObject> objectRef = mappings.get(ddlFile.getUrl());
        if (objectRef == null) return null;

        ConnectionId connectionId = objectRef.getConnectionId();
        return ConnectionHandler.get(connectionId);
    }


    public boolean hasAttachedDDLFiles(DBObjectRef<DBSchemaObject> objectRef) {
        return anyMatch(mappings.values(), o -> Objects.equals(objectRef, o));
    }


    private void checkInvalidAttachedFiles(List<VirtualFile> virtualFiles, DBObjectRef<DBSchemaObject> objectRef) {
        if (virtualFiles == null || virtualFiles.isEmpty()) return;

        List<VirtualFile> obsolete = null;
        for (VirtualFile virtualFile : virtualFiles) {
            if (isNotValid(virtualFile) || !isValidDDLFile(virtualFile, objectRef)) {
                if (obsolete == null) obsolete = new ArrayList<>();
                obsolete.add(virtualFile);
            }
        }

        if (obsolete == null) return;

        virtualFiles.removeAll(obsolete);
        for (VirtualFile virtualFile : obsolete) {
            detachDDLFile(virtualFile);
        }
    }

    private boolean isValidDDLFile(VirtualFile virtualFile, DBObjectRef<DBSchemaObject> objectRef) {
        List<DDLFileType> ddlFileTypes = getDdlFileTypes(objectRef);
        for (DDLFileType ddlFileType : ddlFileTypes) {
            if (ddlFileType.getExtensions().contains(virtualFile.getExtension())) {
                return true;
            }
        }
        return false;
    }

    public void showFileAttachDialog(DBSchemaObject object, List<VirtualFileInfo> fileInfos, boolean showLookupOption, DialogCallback<AttachDDLFileDialog> callback) {
        Dialogs.show(() -> new AttachDDLFileDialog(fileInfos, object, showLookupOption), callback);
    }

    public void showFileDetachDialog(DBSchemaObject object, List<VirtualFileInfo> fileInfos, DialogCallback<DetachDDLFileDialog> callback) {
        Dialogs.show(() -> new DetachDDLFileDialog(fileInfos, object), callback);
    }

    public void attachDDLFile(DBObjectRef<DBSchemaObject> objectRef, VirtualFile virtualFile) {
        if (objectRef == null) return;

        // avoid initialising inside editor creation (slow operation assertions since 23.3)
        Documents.cacheDocument(virtualFile);

        mappings.put(virtualFile.getUrl(), objectRef);
        Project project = getProject();
        ProjectEvents.notify(project,
                DDLFileAttachmentManagerListener.TOPIC,
                (listener) -> listener.ddlFileAttached(project, virtualFile));
    }

    public void detachDDLFile(VirtualFile virtualFile) {
        DBObjectRef<DBSchemaObject> objectRef = mappings.remove(virtualFile.getUrl());

        if (objectRef != null) {
            // map last used connection/schema
            FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(getProject());
            ConnectionHandler activeConnection = contextManager.getConnection(virtualFile);
            if (activeConnection == null) {
                DBSchemaObject schemaObject = objectRef.get();
                if (schemaObject != null) {
                    ConnectionHandler connection = schemaObject.getConnection();
                    contextManager.setConnection(virtualFile, connection);
                    contextManager.setDatabaseSchema(virtualFile, schemaObject.getSchemaId());
                }
            }
        }

        Project project = getProject();
        ProjectEvents.notify(project,
                DDLFileAttachmentManagerListener.TOPIC,
                (listener) -> listener.ddlFileDetached(project, virtualFile));
    }

    private List<VirtualFile> lookupApplicableDDLFiles(@NotNull DBObjectRef<DBSchemaObject> objectRef) {
        List<VirtualFile> fileList = new ArrayList<>();

        Project project = getProject();
        List<DDLFileType> ddlFileTypes = getDdlFileTypes(objectRef);
        for (DDLFileType ddlFileType : ddlFileTypes) {
            for (String extension : ddlFileType.getExtensions()) {
                String fileName = objectRef.getFileName() + '.' + extension;
                FileSearchRequest searchRequest = FileSearchRequest.forNames(fileName);
                VirtualFile[] files = VirtualFiles.findFiles(project, searchRequest);
                fileList.addAll(Arrays.asList(files));
            }
        }
        return fileList;
    }

    @NotNull
    private List<DDLFileType> getDdlFileTypes(@NotNull DBObjectRef<DBSchemaObject> objectRef) {
        DBObjectType objectType = objectRef.getObjectType();
        DDLFileManager ddlFileManager = DDLFileManager.getInstance(getProject());
        return ddlFileManager.getDDLFileTypes(objectType);
    }

    public List<VirtualFile> lookupDetachedDDLFiles(DBObjectRef<DBSchemaObject> object) {
        List<String> fileUrls = getAttachedFileUrls(object);
        List<VirtualFile> virtualFiles = lookupApplicableDDLFiles(object);
        List<VirtualFile> detachedVirtualFiles = new ArrayList<>();
        for (VirtualFile virtualFile : virtualFiles) {
            if (!fileUrls.contains(virtualFile.getUrl())) {
                detachedVirtualFiles.add(virtualFile);
            }
        }

        return detachedVirtualFiles;
    }

    public void createDDLFile(@NotNull DBObjectRef<DBSchemaObject> objectRef) {
        DDLFileNameProvider fileNameProvider = getDDLFileNameProvider(objectRef);
        Project project = getProject();

        if (fileNameProvider != null) {
            FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            descriptor.setTitle("Select New DDL-File Location");

            VirtualFile[] selectedDirectories = FileChooser.chooseFiles(descriptor, project, null);
            if (selectedDirectories.length > 0) {
                String fileName = fileNameProvider.getFileName();
                VirtualFile parentDirectory = selectedDirectories[0];
                DBSchemaObject object = objectRef.ensure();

                try {
                    VirtualFile virtualFile = parentDirectory.createChildData(this, fileName);
                    attachDDLFile(objectRef, virtualFile);
                    DBEditableObjectVirtualFile editableObjectFile = object.getEditableVirtualFile();
                    updateDDLFiles(editableObjectFile);

                } catch (IOException e) {
                    conditionallyLog(e);
                    Messages.showErrorDialog(project, "Could not create file " + parentDirectory + File.separator + fileName + ".", e);
                }

                DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
                editorManager.reopenEditor(object);
            }
        } else {
            showMissingFileAssociations(objectRef);
        }
    }

    public void updateDDLFiles(DBEditableObjectVirtualFile databaseFile) {
        Project project = getProject();
        DDLFileSettings ddlFileSettings = DDLFileSettings.getInstance(project);
        if (!ddlFileSettings.getGeneralSettings().isDdlFilesSynchronizationEnabled()) return;

        DDLFileManager ddlFileManager = DDLFileManager.getInstance(project);
        List<VirtualFile> ddlFiles = databaseFile.getAttachedDDLFiles();
        if (ddlFiles == null || ddlFiles.isEmpty()) return;

        for (VirtualFile ddlFile : ddlFiles) {
            DDLFileType ddlFileType = ddlFileManager.getDDLFileTypeForExtension(ddlFile.getExtension());
            DBContentType fileContentType = ddlFileType.getContentType();

            StringBuilder buffer = new StringBuilder();
            if (fileContentType.isBundle()) {
                DBContentType[] contentTypes = fileContentType.getSubContentTypes();
                for (DBContentType contentType : contentTypes) {
                    DBSourceCodeVirtualFile sourceCodeFile = databaseFile.getContentFile(contentType);
                    if (sourceCodeFile == null) continue;

                    String statement = ddlFileManager.createDDLStatement(sourceCodeFile, contentType);
                    if (Strings.isNotBlank(statement)) {
                        buffer.append(statement);
                        buffer.append('\n');
                    }
                    if (contentType != contentTypes[contentTypes.length - 1]) buffer.append('\n');
                }
            } else {
                DBSourceCodeVirtualFile sourceCodeFile = databaseFile.getContentFile(fileContentType);
                if (sourceCodeFile != null) {
                    buffer.append(ddlFileManager.createDDLStatement(sourceCodeFile, fileContentType));
                    buffer.append('\n');
                }
            }
            Document document = Documents.getDocument(ddlFile);
            if (document != null) {
                Documents.setText(document, buffer);
            }
        }
    }

    public void attachDDLFiles(DBObjectRef<DBSchemaObject> objectRef) {
        Progress.prompt(
                getProject(),
                objectRef, true,
                "Attaching DDL Files",
                "Attaching DDL files to " + objectRef.getQualifiedNameWithType(), t -> {
                    DDLFileNameProvider ddlFileNameProvider = getDDLFileNameProvider(objectRef);
                    if (ddlFileNameProvider == null) return;

                    List<VirtualFile> files = lookupDetachedDDLFiles(objectRef);
                    if (files.isEmpty()) {
                        List<String> fileUrls = getAttachedFileUrls(objectRef);

                        StringBuilder message = new StringBuilder();
                        message.append(fileUrls.isEmpty() ?
                                "No DDL Files were found in " :
                                "No additional DDL Files were found in ");
                        message.append("project scope.");

                        if (!fileUrls.isEmpty()) {
                            message.append("\n\nFollowing files are already attached to ");
                            message.append(objectRef.getQualifiedNameWithType());
                            message.append(':');
                            for (String fileUrl : fileUrls) {
                                message.append('\n');
                                message.append(VirtualFiles.ensureFilePath(fileUrl));
                            }
                        }

                        String[] options = {"Create New...", "Cancel"};
                        Messages.showInfoDialog(getProject(),
                                "No DDL files found",
                                message.toString(), options, 0,
                                option -> when(option == 0, () -> createDDLFile(objectRef)));
                    } else {
                        List<VirtualFileInfo> fileInfos = VirtualFileInfo.fromFiles(files, getProject());
                        DBSchemaObject object = objectRef.ensure();
                        showFileAttachDialog(object, fileInfos, false, (dialog, exitCode) -> {
                            if (exitCode == DialogWrapper.CANCEL_EXIT_CODE) return;

                            DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(object.getProject());
                            editorManager.reopenEditor(object);
                        });
                    }
                });

    }

    public void detachDDLFiles(DBObjectRef<DBSchemaObject> objectRef) {
        Progress.prompt(
                getProject(),
                objectRef, true,
                "Detaching DDL Files",
                "Detaching DDL files from " + objectRef.getQualifiedNameWithType(), t -> {
                    List<VirtualFile> files = getAttachedDDLFiles(objectRef);
                    if (files == null) return;

                    List<VirtualFileInfo> fileInfos = VirtualFileInfo.fromFiles(files, getProject());
                    DBSchemaObject object = objectRef.ensure();
                    showFileDetachDialog(object, fileInfos, (dialog, exitCode) -> {
                            if (exitCode == DialogWrapper.CANCEL_EXIT_CODE) return;

                            DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(object.getProject());
                            editorManager.reopenEditor(object);
                        });
        });
    }

    @Nullable
    private DDLFileNameProvider getDDLFileNameProvider(DBObjectRef<DBSchemaObject> objectRef) {
        List<DDLFileType> ddlFileTypes = getDdlFileTypes(objectRef);
        if (ddlFileTypes.isEmpty()) {
            showMissingFileAssociations(objectRef);

        } else if (ddlFileTypes.size() == 1 && ddlFileTypes.get(0).getExtensions().size() == 1) {
            DDLFileType ddlFileType = ddlFileTypes.get(0);
            String extension = ddlFileType.getFirstExtension();
            return new DDLFileNameProvider(objectRef, ddlFileType, extension);
        } else if (ddlFileTypes.size() > 1) {
            List<DDLFileNameProvider> fileNameProviders = new ArrayList<>();
            for (DDLFileType ddlFileType : ddlFileTypes) {
                for (String extension : ddlFileType.getExtensions()) {
                    DDLFileNameProvider fileNameProvider = new DDLFileNameProvider(objectRef, ddlFileType, extension);
                    fileNameProviders.add(fileNameProvider);
                }
            }

            return Dispatch.call(() -> {
                SelectFromListDialog fileTypeDialog = new SelectFromListDialog(
                        getProject(),
                        fileNameProviders.toArray(),
                        Lists.BASIC_TO_STRING_ASPECT,
                        "Select DDL File Type",
                        ListSelectionModel.SINGLE_SELECTION);
                JList list = Failsafe.nd((JList) fileTypeDialog.getPreferredFocusedComponent());
                list.setCellRenderer(new DDLFileNameListCellRenderer());
                fileTypeDialog.show();
                Object[] selection = fileTypeDialog.getSelection();
                if (selection == null) throw new ProcessCanceledException();
                return (DDLFileNameProvider) selection[0];
            });
        }
        return null;
    }

    public void showMissingFileAssociations(DBObjectRef<DBSchemaObject> objectRef) {
        Messages.showWarningDialog(
                getProject(),
                "No DDL File Type Association",
                "No DDL file type is configured for database " + objectRef.getObjectType().getListName() +
                        ".\nPlease check the DDL file association in Project Settings > DB Navigator > DDL File Settings.",
                options("Open Settings...", "Cancel"), 0,
                option -> when(option == 0, () -> {
                    ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(getProject());
                    settingsManager.openProjectSettings(ConfigId.DDL_FILES);
                }));
    }

    private List<String> getAttachedFileUrls(DBObjectRef<DBSchemaObject> objectRef) {
        List<String> fileUrls = new ArrayList<>();
        for (val entry : mappings.entrySet()) {
            String fileUrl = entry.getKey();
            if (entry.getValue().equals(objectRef)) {
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }

    /************************************************
     *               VirtualFileListener            *
     ************************************************/
    @Deprecated // TODO cleanup
    private final VirtualFileListener virtualFileListener = new VirtualFileListener() {
        @Override
        public void fileDeleted(@NotNull VirtualFileEvent event) {
            processFileDeletedEvent(event.getFile());
        }
    };


    private void processFileDeletedEvent(@NotNull VirtualFile file) {
        DBObjectRef<DBSchemaObject> objectRef = mappings.get(file.getUrl());
        DBSchemaObject object = DBObjectRef.get(objectRef);
        if (object == null) return;

        detachDDLFile(file);
        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(getProject());
        editorManager.reopenEditor(object);
    }

    /*********************************************
     *            PersistentStateComponent       *
     *********************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        for (val entry : mappings.entrySet()) {
            String fileUrl = entry.getKey();
            val objectRef = entry.getValue();

            Element childElement = newElement(element, "mapping");
            childElement.setAttribute("file-url", fileUrl);
            objectRef.writeState(childElement);
        }

        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        for (Element child : element.getChildren()) {
            String fileUrl = stringAttribute(child, "file-url");
            if (StringUtil.isEmpty(fileUrl)) {
                // TODO backward compatibility. Do cleanup
                fileUrl = stringAttribute(child, "file");
            }

            if (StringUtil.isNotEmpty(fileUrl)) {
                fileUrl = VirtualFiles.ensureFileUrl(fileUrl);
                DBObjectRef<DBSchemaObject> objectRef = DBObjectRef.from(child);
                if (objectRef != null) mappings.put(fileUrl, objectRef);
            }
        }
        Background.run(getProject(), () -> cleanupFileMappings());
    }

    public void cleanupFileMappings(){
        VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
        mappings.keySet().removeIf(url -> virtualFileManager.findFileByUrl(url) == null);
    }


    public void warmUpAttachedDDLFiles(VirtualFile file) {
        if (file instanceof DBEditableObjectVirtualFile) {
            DBEditableObjectVirtualFile objectFile = (DBEditableObjectVirtualFile) file;
            if (isFileOpened(objectFile.getObject())) return;

            List<VirtualFile> files = getAttachedDDLFiles(objectFile.getObjectRef());
            Documents.cacheDocuments(files);
        }

    }

}
