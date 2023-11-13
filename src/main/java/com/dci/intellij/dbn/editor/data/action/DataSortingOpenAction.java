package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.state.DatasetEditorStateManager;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class DataSortingOpenAction extends AbstractDataEditorAction {

    DataSortingOpenAction() {
        super("Data Sorting...", Icons.DATA_SORTING);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatasetEditor datasetEditor) {
        DBDataset dataset = datasetEditor.getDataset();
        DatasetEditorStateManager stateManager = DatasetEditorStateManager.getInstance(datasetEditor.getProject());
        stateManager.openSortingDialog(datasetEditor);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable DatasetEditor datasetEditor) {
        presentation.setText("Data Sorting...");

        boolean enabled =
                isValid(datasetEditor) &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading();
        presentation.setEnabled(enabled);
    }
}
