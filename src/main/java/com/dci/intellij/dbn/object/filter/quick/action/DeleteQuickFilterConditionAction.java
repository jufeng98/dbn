package com.dci.intellij.dbn.object.filter.quick.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.object.filter.quick.ui.ObjectQuickFilterConditionForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class DeleteQuickFilterConditionAction extends DumbAwareAction {
    private final ObjectQuickFilterConditionForm conditionForm;

    public DeleteQuickFilterConditionAction(ObjectQuickFilterConditionForm conditionForm) {
        this.conditionForm = conditionForm;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setIcon(Icons.DATASET_FILTER_CONDITION_REMOVE);
        e.getPresentation().setText("Remove Condition");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        conditionForm.remove();
    }

}
