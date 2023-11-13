package com.dci.intellij.dbn.editor.data.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.DatasetEditorManager;
import com.dci.intellij.dbn.editor.data.filter.DatasetFilterInput;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRecordsOpenAction extends DumbAwareAction{
    private final DatasetFilterInput filterInput;

    AbstractRecordsOpenAction(String text, DatasetFilterInput filterInput) {
        super();
        this.filterInput = filterInput;
        Presentation presentation = getTemplatePresentation();
        presentation.setText(text, false);
        presentation.setIcon(Icons.DBO_TABLE);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = filterInput.getDataset().getProject();
        DatasetEditorManager datasetEditorManager = DatasetEditorManager.getInstance(project);
        datasetEditorManager.openDataEditor(filterInput);
    }
}
