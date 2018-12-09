package com.dci.intellij.dbn.editor.data;

import com.dci.intellij.dbn.common.AbstractProjectComponent;
import com.dci.intellij.dbn.common.dispose.FailsafeUtil;
import com.dci.intellij.dbn.common.options.setting.SettingsUtil;
import com.dci.intellij.dbn.common.util.EventUtil;
import com.dci.intellij.dbn.common.util.MessageUtil;
import com.dci.intellij.dbn.data.record.ColumnSortingType;
import com.dci.intellij.dbn.data.record.DatasetRecord;
import com.dci.intellij.dbn.data.record.navigation.RecordNavigationTarget;
import com.dci.intellij.dbn.data.record.navigation.action.RecordNavigationActionGroup;
import com.dci.intellij.dbn.data.record.ui.RecordViewerDialog;
import com.dci.intellij.dbn.editor.EditorProviderId;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.options.DataEditorSettings;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.DBTable;
import com.dci.intellij.dbn.object.DBView;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.DatabaseFileSystem;
import com.dci.intellij.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.DELIBERATE_ACTION;
import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.PRESERVE_CHANGES;
import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.REBUILD;
import static com.dci.intellij.dbn.editor.data.DatasetLoadInstruction.USE_CURRENT_FILTER;

@State(
    name = DatasetEditorManager.COMPONENT_NAME,
        storages = {
                @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/dbnavigator.xml", scheme = StorageScheme.DIRECTORY_BASED),
                @Storage(file = StoragePathMacros.PROJECT_FILE)}
)
public class DatasetEditorManager extends AbstractProjectComponent implements PersistentStateComponent<Element> {
    public static final String COMPONENT_NAME = "DBNavigator.Project.DataEditorManager";

    private static final DatasetLoadInstructions INITIAL_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(USE_CURRENT_FILTER, PRESERVE_CHANGES, REBUILD);
    private static final DatasetLoadInstructions RELOAD_LOAD_INSTRUCTIONS = new DatasetLoadInstructions(USE_CURRENT_FILTER, PRESERVE_CHANGES, DELIBERATE_ACTION);

    private ColumnSortingType recordViewColumnSortingType = ColumnSortingType.BY_INDEX;
    private boolean valuePreviewTextWrapping = true;
    private boolean valuePreviewPinned = false;

    private DatasetEditorManager(Project project) {
        super(project);
    }

    public static DatasetEditorManager getInstance(@NotNull Project project) {
        return FailsafeUtil.getComponent(project, DatasetEditorManager.class);
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
        DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
        filterManager.createBasicFilter(filterInput);
        DatabaseFileSystem.getInstance().openEditor(dataset, EditorProviderId.DATA, true);
    }
    
    public void openRecordViewer(DatasetFilterInput filterInput) {
        try {
            DatasetRecord record = new DatasetRecord(filterInput);
            RecordViewerDialog dialog = new RecordViewerDialog(getProject(), record);
            dialog.show();
        } catch (SQLException e) {
            MessageUtil.showErrorDialog(getProject(), "Could not load record details", e);
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
                    DataManager.getInstance().getDataContext(component),
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

    public void setRecordViewColumnSortingType(ColumnSortingType columnSorting) {
        recordViewColumnSortingType = columnSorting;
    }

    public ColumnSortingType getRecordViewColumnSortingType() {
        return recordViewColumnSortingType;
    }

    public boolean isValuePreviewTextWrapping() {
        return valuePreviewTextWrapping;
    }

    public void setValuePreviewTextWrapping(boolean valuePreviewTextWrapping) {
        this.valuePreviewTextWrapping = valuePreviewTextWrapping;
    }

    public boolean isValuePreviewPinned() {
        return valuePreviewPinned;
    }

    public void setValuePreviewPinned(boolean valuePreviewPinned) {
        this.valuePreviewPinned = valuePreviewPinned;
    }

    /****************************************
    *             ProjectComponent          *
    *****************************************/
    @NonNls
    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    FileEditorManagerAdapter fileEditorListener = new FileEditorManagerAdapter() {
        @Override
        public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
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
        public void selectionChanged(@NotNull FileEditorManagerEvent event) {
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

    @Override
    public void initComponent() {
        EventUtil.subscribe(getProject(), this, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorListener);
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getState() {
        Element element = new Element("state");
        SettingsUtil.setEnum(element, "record-view-column-sorting-type", recordViewColumnSortingType);
        SettingsUtil.setBoolean(element, "value-preview-text-wrapping", valuePreviewTextWrapping);
        SettingsUtil.setBoolean(element, "value-preview-pinned", valuePreviewPinned);
        return element;
    }

    @Override
    public void loadState(Element element) {
        recordViewColumnSortingType = SettingsUtil.getEnum(element, "record-view-column-sorting-type", recordViewColumnSortingType);
        valuePreviewTextWrapping = SettingsUtil.getBoolean(element, "value-preview-text-wrapping", valuePreviewTextWrapping);
        valuePreviewTextWrapping = SettingsUtil.getBoolean(element, "value-preview-pinned", valuePreviewPinned);
    }

}
