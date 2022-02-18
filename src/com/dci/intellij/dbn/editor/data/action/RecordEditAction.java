package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecordEditAction extends AbstractDataEditorAction {

    public RecordEditAction() {
        super("Edit Record", Icons.DATA_EDITOR_EDIT_RECORD);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatasetEditor datasetEditor) {
        datasetEditor.openRecordEditor();
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable DatasetEditor datasetEditor) {
        presentation.setText("Edit Record");

        boolean enabled =
                Failsafe.check(datasetEditor) &&
                datasetEditor.getConnection().isConnected() &&
                datasetEditor.getEditorTable().getSelectedRow() != -1 &&
                !datasetEditor.isInserting() &&
                !datasetEditor.isLoading() &&
                !datasetEditor.isDirty();
        presentation.setEnabled(enabled);

    }
}