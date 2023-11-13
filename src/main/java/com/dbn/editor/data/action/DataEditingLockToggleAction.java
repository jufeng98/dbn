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

    public DataEditingLockToggleAction() {
        super("Lock / Unlock Editing", null, Icons.DATA_EDITOR_LOCK_EDITING);
    }

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
        } else {
            boolean isEnvironmentReadonlyData = datasetEditor.getDataset().getEnvironmentType().isReadonlyData();
            presentation.setVisible(!datasetEditor.isReadonlyData() && !isEnvironmentReadonlyData);
            presentation.setText(isSelected(e) ? "Unlock Editing" : "Lock Editing");
            boolean enabled = !datasetEditor.isInserting();
            presentation.setEnabled(enabled);
        }
    }

    private static DatasetEditor getDatasetEditor(AnActionEvent e) {
        FileEditor fileEditor = Lookups.getFileEditor(e);
        return fileEditor instanceof DatasetEditor ? (DatasetEditor) fileEditor : null;
    }
}