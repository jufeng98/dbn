package com.dbn.editor.data.state;

import com.dbn.DatabaseNavigator;
import com.dbn.common.component.PersistentState;
import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.util.Dialogs;
import com.dbn.editor.data.DatasetEditor;
import com.dbn.editor.data.state.column.ui.DatasetColumnSetupDialog;
import com.dbn.editor.data.state.sorting.ui.DatasetEditorSortingDialog;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dbn.common.component.Components.projectService;

@State(
    name = DatasetEditorStateManager.COMPONENT_NAME,
    storages = @Storage(DatabaseNavigator.STORAGE_FILE)
)
public class DatasetEditorStateManager extends ProjectComponentBase implements PersistentState {

    public static final String COMPONENT_NAME = "DBNavigator.Project.DatasetEditorStateManager";

    private DatasetEditorStateManager(Project project) {
        super(project, COMPONENT_NAME);
    }


    public static DatasetEditorStateManager getInstance(@NotNull Project project) {
        return projectService(project, DatasetEditorStateManager.class);
    }

    public void openSortingDialog(@NotNull DatasetEditor datasetEditor) {
        Dialogs.show(() -> new DatasetEditorSortingDialog(datasetEditor));
    }

    public void openColumnSetupDialog(@NotNull DatasetEditor datasetEditor) {
        Dialogs.show(() -> new DatasetColumnSetupDialog(datasetEditor));
    }

    /*********************************************
     *            PersistentStateComponent       *
     *********************************************/
    @Nullable
    @Override
    public Element getComponentState() {
        return null;
    }

    @Override
    public void loadComponentState(@NotNull Element element) {
    }

}
