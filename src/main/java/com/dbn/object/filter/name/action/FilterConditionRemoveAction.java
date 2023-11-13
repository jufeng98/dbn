package com.dbn.object.filter.name.action;

import com.dbn.common.icon.Icons;
import com.dbn.object.filter.name.FilterCondition;
import com.dbn.object.filter.name.ObjectNameFilter;
import com.dbn.object.filter.name.ObjectNameFilterManager;
import com.dbn.object.filter.name.ui.ObjectNameFilterSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class FilterConditionRemoveAction extends AbstractObjectFilterAction {

    public FilterConditionRemoveAction(ObjectNameFilterSettingsForm settingsForm) {
        super("Remove", Icons.ACTION_REMOVE, settingsForm);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        Object selection = getSelection();
        if (selection instanceof FilterCondition) {
            FilterCondition filterCondition = (FilterCondition) selection;

            ObjectNameFilterManager filterManager = ObjectNameFilterManager.getInstance(project);
            filterManager.removeFilterCondition(filterCondition, settingsForm);
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        Object selection = getSelection();
        if (selection instanceof ObjectNameFilter) {
            presentation.setText("Remove Filter");
            presentation.setEnabled(true);
        } else if (selection instanceof FilterCondition) {
            presentation.setText("Remove Condition");
            presentation.setEnabled(true);
        } else {
            presentation.setEnabled(false);
        }
    }

}
