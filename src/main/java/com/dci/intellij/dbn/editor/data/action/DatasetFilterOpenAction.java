package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.DatasetEditor;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterType;
import com.dci.intellij.dbn.object.DBDataset;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class DatasetFilterOpenAction extends DumbAwareAction {
    private DatasetEditor datasetEditor;
    DatasetFilterOpenAction(DatasetEditor datasetEditor) {
        super("Manage Filters...", null, Icons.ACTION_EDIT);
        this.datasetEditor = datasetEditor;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (datasetEditor != null) {
            DBDataset dataset = datasetEditor.getDataset();
            DatasetFilterManager filterManager = DatasetFilterManager.getInstance(dataset.getProject());
            filterManager.openFiltersDialog(dataset, false, false, DatasetFilterType.NONE);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        boolean enabled = datasetEditor != null && !datasetEditor.isInserting();
        e.getPresentation().setEnabled(enabled);

    }
}
