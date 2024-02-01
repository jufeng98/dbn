package com.dbn.object.filter.quick.action;

import com.dbn.common.action.BasicAction;
import com.dbn.common.icon.Icons;
import com.dbn.object.filter.quick.ui.ObjectQuickFilterConditionForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteQuickFilterConditionAction extends BasicAction {
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
