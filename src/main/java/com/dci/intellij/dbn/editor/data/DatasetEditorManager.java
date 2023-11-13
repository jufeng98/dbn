package com.dci.intellij.dbn.editor.data;

import com.dci.intellij.dbn.DatabaseNavigator;
import com.dci.intellij.dbn.common.component.PersistentState;
import com.dci.intellij.dbn.common.component.ProjectComponentBase;
import com.dci.intellij.dbn.common.event.ProjectEvents;
import com.dci.intellij.dbn.common.listener.DBNFileEditorManagerListener;
import com.dci.intellij.dbn.common.options.setting.Settings;
import com.dci.intellij.dbn.common.util.Context;
import com.dci.intellij.dbn.common.util.Dialogs;
import com.dci.intellij.dbn.common.util.Messages;
import com.dci.intellij.dbn.data.record.ColumnSortingType;
import com.dci.intellij.dbn.data.record.DatasetRecord;
import com.dci.intellij.dbn.data.record.navigation.RecordNavigationTarget;
import com.dci.intellij.dbn.data.record.navigation.action.RecordNavigationActionGroup;
import com.dci.intellij.dbn.data.record.ui.RecordViewerDialog;
import com.dci.intellij.dbn.editor.DatabaseFileEditorManager;
import com.dci.intellij.dbn.editor.EditorProviderId;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBTable;
import com.dci.intellij.dbn.object.DBView;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import static com.dci.intellij.dbn.common.component.Components.projectService;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;
import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.*;

@State(
    name = DatasetEditorManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
@Getter
@Setter
public class DatasetEditorManager extends ProjectComponentBase implements PersistentState {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DataEditorManager";

    private static final DatasetLoadInstructions INITIAL_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(USE_CURRENT_FILTER, PRESERVE_CHANGES, REBUILD);
    private static final DatasetLoadInstructions RELOAD_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(USE_CURRENT_FILTER, PRESERVE_CHANGES, DELIBERATE_ACTION);

    private ColumnSortingType recordViewColumnSortingType = ColumnSortingType.BY_INDEX;
    private boolean valuePreviewTextWrapping = true;
    private boolean valuePreviewPinned = false;

    private DatasetEditorManager(Project project) {
        super(project, COMPONENT_NAME);
        ProjectEvents.subscribe(project, this, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener());
    }

    public static DatasetEditorManager getInstance(@NotNull Project project) {
        return projectService(project, DatasetEditorManager.class);
    }

    @NotNull
    private static FileEditorManagerListener fileEditorManagerListener() {
        return new DBNFileEditorManagerListener() {
            @Override
            public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                if (file instanceof DBEditableObjectVirtualFile) {
                    DBEditableObjectVirtualFile editableObjectFile = (DBEditableObjectVirtualFile) file;
                    DBSchemaObject object = editableObjectFile.getObject();
                    if (object instanceof DBDataset) {
                        FileEditor[] fileEditors = source.getEditors(file);
                        for (FileEditor fileEditor : fileEditors) {
                            if (fileEditor instanceof DatasetEditor) {
                                DatasetEditor datasetEditor = (DatasetEditor) fileEditor;
                                if (object instanceof DBTable || editableObjectFile.getSelectedEditorProviderId() == EditorProviderId.DATA) {
                                    datasetEditor.loadData(INITIAL_LOAD_INSTRUCTIONS);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void whenSelectionChanged(@NotNull FileEditorManagerEvent event) {
                FileEditor newEditor = event.getNewEditor();
                if (newEditor instanceof DatasetEditor) {
                    DatasetEditor datasetEditor = (DatasetEditor) newEditor;
                    DBDataset dataset = datasetEditor.getDataset();
                    if (dataset instanceof DBView) {
                        if (!datasetEditor.isLoaded() && !datasetEditor.isLoading()) {
                            datasetEditor.loadData(INITIAL_LOAD_INSTRUCTIONS);
                        }
                    }
                }
            }
        };
    }

    public void reloadEditorData(DBDataset dataset) {
        VirtualFile file = dataset.getVirtualFile();
        FileEditor[] fileEditors = FileEditorManager.getInstance(getProject()).getEditors(file);
        for (FileEditor fileEditor : fileEditors) {
            if (fileEditor instanceof DatasetEditor) {
                DatasetEditor datasetEditor = (DatasetEditor) fileEditor;
                datasetEditor.loadData(RELOAD_LOAD_INSTRUCTIONS);
                break;
            }
        }
    }

    public void openDataEditor(DatasetFilterInput filterInput) {
        DBDataset dataset = filterInput.getDataset();
        Project project = dataset.getProject();

        DatasetFilterManager filterManager = DatasetFilterManager.getInstance(project);
        filterManager.createBasicFilter(filterInput);

        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
        editorManager.connectAndOpenEditor(dataset, EditorProviderId.DATA, false, true);
    }
    
    public void openRecordViewer(DatasetFilterInput filterInput) {
        try {
            DatasetRecord record = new DatasetRecord(filterInput);
            Dialogs.show(() -> new RecordViewerDialog(getProject(), record));
        } catch (SQLException e) {
            conditionallyLog(e);
            Messages.showErrorDialog(getProject(), "Could not load record details", e);
        }
    }

    public void navigateToRecord(DatasetFilterInput filterInput, InputEvent inputEvent) {
        DataEditorSettings settings = DataEditorSettings.getInstance(getProject());
        RecordNavigationTarget navigationTarget = settings.getRecordNavigationSettings().getNavigationTarget();
        if (navigationTarget == RecordNavigationTarget.EDITOR) {
            openDataEditor(filterInput);
        } else if (navigationTarget == RecordNavigationTarget.VIEWER) {
            openRecordViewer(filterInput);
        } else if (navigationTarget == RecordNavigationTarget.ASK) {
            ActionGroup actionGroup = new RecordNavigationActionGroup(filterInput);
            Component component = (Component) inputEvent.getSource();

            ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                    "Select Navigation Target",
                    actionGroup,
                    Context.getDataContext(component),
                    JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                    true, null, 10);

            if (inputEvent instanceof MouseEvent) {
                MouseEvent mouseEvent = (MouseEvent) inputEvent;
                popup.showInScreenCoordinates(component, mouseEvent.getLocationOnScreen());
                        
            } else {
                popup.show(component);
            }
        }
    }
    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        Element element = new Element("state");
        Settings.setEnum(element, "record-view-column-sorting-type", recordViewColumnSortingType);
        Settings.setBoolean(element, "value-preview-text-wrapping", valuePreviewTextWrapping);
        Settings.setBoolean(element, "value-preview-pinned", valuePreviewPinned);
        return element;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
        recordViewColumnSortingType = Settings.getEnum(element, "record-view-column-sorting-type", recordViewColumnSortingType);
        valuePreviewTextWrapping = Settings.getBoolean(element, "value-preview-text-wrapping", valuePreviewTextWrapping);
        valuePreviewTextWrapping = Settings.getBoolean(element, "value-preview-pinned", valuePreviewPinned);
    }

}
