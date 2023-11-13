package com.dci.intellij.dbn.editor.data.state.sorting.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetEditorSortingForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;


public class AddSortingColumnAction extends DumbAwareAction {
    private final DatasetEditorSortingForm form;

    public AddSortingColumnAction(DatasetEditorSortingForm form) {
        super("Add Sorting Column ", null, Icons.DATASET_FILTER_CONDITION_NEW);
        this.form = form;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        form.addSortingColumn(null);
    }
}
