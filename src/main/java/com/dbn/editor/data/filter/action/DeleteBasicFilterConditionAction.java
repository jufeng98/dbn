package com.dbn.editor.data.filter.action;

import com.dbn.common.action.BasicAction;
import com.dbn.common.icon.Icons;
import com.dbn.editor.data.filter.ui.DatasetBasicFilterConditionForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteBasicFilterConditionAction extends BasicAction {
    private DatasetBasicFilterConditionForm conditionForm;

    public DeleteBasicFilterConditionAction(DatasetBasicFilterConditionForm conditionForm) {
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
