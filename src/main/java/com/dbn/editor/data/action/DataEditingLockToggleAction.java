package com.dbn.editor.data.action;

import com.dbn.common.action.Lookups;
import com.dbn.common.action.ToggleAction;
import com.dbn.common.icon.Icons;
import com.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DataEditingLockToggleAction extends ToggleAction implements DumbAware {

    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        DatasetEditor datasetEditor = getDatasetEditor(e);
        return datasetEditor != null && datasetEditor.isReadonly();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean selected) {
        DatasetEditor datasetEditor = getDatasetEditor(e);
        if (datasetEditor != null) datasetEditor.setReadonly(selected);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        DatasetEditor datasetEditor = getDatasetEditor(e);
        Presentation presentation = e.getPresentation();
        Project project = e.getProject();
        if (project == null || datasetEditor == null) {
            presentation.setEnabled(false);
            presentation.setIcon(Icons.DATA_EDITOR_LOCKED);
            presentation.setText("Lock / Unlock Editing");
        } else {
            boolean isEnvironmentReadonlyData = datasetEditor.getDataset().getEnvironmentType().isReadonlyData();
            presentation.setVisible(!datasetEditor.isReadonlyData() && !isEnvironmentReadonlyData);
            boolean selected = isSelected(e);
            presentation.setText(selected ? "Unlock Editing" : "Lock Editing");
            presentation.setIcon(selected ? Icons.DATA_EDITOR_LOCKED : Icons.DATA_EDITOR_UNLOCKED);
            boolean enabled = !datasetEditor.isInserting();
            presentation.setEnabled(enabled);
        }

    }

    private static DatasetEditor getDatasetEditor(AnActionEvent e) {
        FileEditor fileEditor = Lookups.getFileEditor(e);
        return fileEditor instanceof DatasetEditor ? (DatasetEditor) fileEditor : null;
    }
}