package com.dbn.editor.data.filter.action;

import com.dbn.common.icon.Icons;
import com.dbn.editor.data.filter.ui.DatasetBasicFilterForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;


public class CreateBasicFilterConditionAction extends DumbAwareAction {
    private final DatasetBasicFilterForm filterForm;

    public CreateBasicFilterConditionAction(DatasetBasicFilterForm filterForm) {
        super("Add condition", null, Icons.DATASET_FILTER_CONDITION_NEW);
        this.filterForm = filterForm;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        filterForm.addConditionPanel(null);
    }
}
