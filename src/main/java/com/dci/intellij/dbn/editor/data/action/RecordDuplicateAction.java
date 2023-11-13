package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.dispose.Checks;
import com.dci.intellij.dbn.common.environment.EnvironmentManager;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecordDuplicateAction extends AbstractDataEditorAction {

    public RecordDuplicateAction() {
        super("Duplicate record", Icons.DATA_EDITOR_DUPLICATE_RECORD);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatasetEditor datasetEditor) {
        datasetEditor.duplicateRecord();
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable DatasetEditor datasetEditor) {
        presentation.setText("Duplicate Record");
        if (Checks.isValid(datasetEditor) && datasetEditor.getConnection().isConnected()) {
            presentation.setEnabled(true);
            EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
            boolean isEnvironmentReadonlyData = environmentManager.isReadonly(datasetEditor.getDataset(), DBContentType.DATA);

            presentation.setVisible(!isEnvironmentReadonlyData && !datasetEditor.isReadonlyData());
            if (datasetEditor.isInserting() || datasetEditor.isLoading() || datasetEditor.isDirty() || datasetEditor.isReadonly()) {
                presentation.setEnabled(false);
            } else {
                DatasetEditorTable editorTable = datasetEditor.getEditorTable();
                int[] selectedRows = editorTable.getSelectedRows();
                presentation.setEnabled(selectedRows != null && selectedRows.length == 1 && selectedRows[0] < editorTable.getModel().getRowCount());
            }
        } else {
            presentation.setEnabled(false);
        }
    }
}