package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.environment.EnvironmentManager;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.util.Messages;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class DataImportAction extends AbstractDataEditorAction {

    public DataImportAction() {
        super("Import Data", Icons.DATA_IMPORT);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatasetEditor datasetEditor) {
        Messages.showInfoDialog(project, "Not implemented", "Data import is not implemented yet.");
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Presentation presentation, @NotNull Project project, @Nullable DatasetEditor datasetEditor) {
        presentation.setText("Import Data");
        if (isValid(datasetEditor)) {
            EnvironmentManager environmentManager = EnvironmentManager.getInstance(project);
            boolean isEnvironmentReadonlyData = environmentManager.isReadonly(datasetEditor.getDataset(), DBContentType.DATA);
            presentation.setVisible(!isEnvironmentReadonlyData && !datasetEditor.isReadonlyData());
/*
            boolean enabled =
                    datasetEditor.getConnectionHandler().isConnected() &&
                    !datasetEditor.isReadonly() &&
                    !datasetEditor.isInserting();
*/
            boolean enabled = false;
            presentation.setEnabled(enabled);
        } else {
            presentation.setEnabled(false);
        }
    }
}