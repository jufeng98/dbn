package com.dci.intellij.dbn.object.filter.name.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.object.filter.name.CompoundFilterCondition;
import com.dci.intellij.dbn.object.filter.name.ObjectNameFilterManager;
import com.dci.intellij.dbn.object.filter.name.ui.ObjectNameFilterSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class FilterConditionJoinTypeSwitchAction extends AbstractObjectFilterAction {

    public FilterConditionJoinTypeSwitchAction(ObjectNameFilterSettingsForm settingsForm) {
        super("Switch Join Type", Icons.CONDITION_JOIN_TYPE, settingsForm);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        Object selection = getSelection();
        ObjectNameFilterManager filterManager = ObjectNameFilterManager.getInstance(project);
        if (selection instanceof CompoundFilterCondition) {
            CompoundFilterCondition condition = (CompoundFilterCondition) selection;
            filterManager.switchConditionJoinType(condition, settingsForm);
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        Object selection = getSelection();
        if (selection instanceof CompoundFilterCondition) {
            CompoundFilterCondition condition = (CompoundFilterCondition) selection;
            presentation.setEnabled(condition.getConditions().size() > 1);
        } else {
            presentation.setEnabled(false);
        }
    }
}
