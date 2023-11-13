package com.dbn.object.filter.name.action;

import com.dbn.common.icon.Icons;
import com.dbn.object.filter.name.CompoundFilterCondition;
import com.dbn.object.filter.name.FilterCondition;
import com.dbn.object.filter.name.ObjectNameFilter;
import com.dbn.object.filter.name.ObjectNameFilterManager;
import com.dbn.object.filter.name.ui.ObjectNameFilterSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterConditionMoveDownAction extends AbstractObjectFilterAction {

    public FilterConditionMoveDownAction(ObjectNameFilterSettingsForm settingsForm) {
        super("Move Down", Icons.ACTION_MOVE_DOWN, settingsForm);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        Object selection = getSelection();
        if (selection instanceof FilterCondition) {
            FilterCondition condition = (FilterCondition) selection;
            ObjectNameFilterManager filterManager = ObjectNameFilterManager.getInstance(project);
            filterManager.moveFilterConditionDown(condition, settingsForm);
        }
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Object selection = getSelection();
        Presentation presentation = e.getPresentation();
        if (selection instanceof FilterCondition) {
            FilterCondition condition = (FilterCondition) selection;
            if (condition instanceof ObjectNameFilter) {
                ObjectNameFilter filter = (ObjectNameFilter) condition;
                List<ObjectNameFilter> filters = filter.getSettings().getFilters();
                int index = filters.indexOf(filter);
                presentation.setEnabled(index < filters.size() - 1);
            } else {
                CompoundFilterCondition parentCondition = condition.getParent();
                List<FilterCondition> conditions = parentCondition.getConditions();
                int index = conditions.indexOf(condition);
                presentation.setEnabled(index < conditions.size() - 1);
            }
        } else {
            presentation.setEnabled(false);
        }
    }
}
