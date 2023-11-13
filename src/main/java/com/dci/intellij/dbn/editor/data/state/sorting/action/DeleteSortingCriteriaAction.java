package com.dci.intellij.dbn.editor.data.state.sorting.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.data.state.sorting.ui.DatasetSortingColumnForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class DeleteSortingCriteriaAction extends DumbAwareAction {
    private final DatasetSortingColumnForm form;

    public DeleteSortingCriteriaAction(DatasetSortingColumnForm form) {
        this.form = form;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setIcon(Icons.ACTION_CLOSE);
        e.getPresentation().setText("Remove Sorting Criteria");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        form.remove();
    }

}
