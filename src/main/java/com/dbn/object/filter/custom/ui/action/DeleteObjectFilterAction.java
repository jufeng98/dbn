package com.dbn.object.filter.custom.ui.action;

import com.dbn.common.action.BasicAction;
import com.dbn.common.icon.Icons;
import com.dbn.object.filter.custom.ui.ObjectFilterExpressionForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteObjectFilterAction extends BasicAction {
    private final ObjectFilterExpressionForm form;

    public DeleteObjectFilterAction(ObjectFilterExpressionForm conditionForm) {
        this.form = conditionForm;
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setIcon(Icons.DATASET_FILTER_CONDITION_REMOVE);
        e.getPresentation().setText("Remove Filter");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        form.remove();
    }

}
