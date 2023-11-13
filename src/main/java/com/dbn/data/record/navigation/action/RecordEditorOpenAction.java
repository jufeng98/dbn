package com.dbn.data.record.navigation.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.editor.data.DatasetEditorManager;
import com.dbn.editor.data.filter.DatasetFilterInput;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RecordEditorOpenAction extends ProjectAction {
    private DatasetFilterInput filterInput;

    RecordEditorOpenAction(DatasetFilterInput filterInput) {
        super("Editor", null, null);
        this.filterInput = filterInput;
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        DatasetEditorManager datasetEditorManager = DatasetEditorManager.getInstance(project);
        datasetEditorManager.openDataEditor(filterInput);
    }
}
