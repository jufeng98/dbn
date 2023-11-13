package com.dbn.object.filter.name.ui;

import com.dbn.object.filter.name.CompoundFilterCondition;
import com.dbn.object.filter.name.FilterCondition;
import com.dbn.object.filter.name.ObjectNameFilter;
import com.dbn.object.filter.name.SimpleNameFilterCondition;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.*;
import java.util.List;

class FilterSettingsTreeCellRenderer extends ColoredTreeCellRenderer {
    @Override
    public void customizeCellRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof ObjectNameFilter) {
            ObjectNameFilter condition = (ObjectNameFilter) value;
            append(condition.getObjectType().getName().toUpperCase(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            setIcon(condition.getObjectType().getIcon());
        }

        if (value instanceof CompoundFilterCondition) {
            CompoundFilterCondition condition = (CompoundFilterCondition) value;
            List<FilterCondition> conditions = condition.getConditions();
            if (conditions.size() > 1) {
                append(" (" + conditions.size() + " conditions joined with " + condition.getJoinType() + ") ", SimpleTextAttributes.GRAY_ATTRIBUTES);
            }
        }

        if (value instanceof SimpleNameFilterCondition) {
            SimpleNameFilterCondition condition = (SimpleNameFilterCondition) value;

            append(condition.getObjectType().getName().toUpperCase() + "_NAME ", SimpleTextAttributes.REGULAR_ATTRIBUTES);
            append(condition.getOperator().getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
            append(" '" + condition.getPattern() + "' ", new SimpleTextAttributes(0, JBColor.BLUE));

        }

        if (value instanceof FilterCondition) {
            FilterCondition condition = (FilterCondition) value;
            CompoundFilterCondition parentCondition = condition.getParent();
            if (parentCondition != null) {
                List<FilterCondition> conditions = parentCondition.getConditions();
                if (conditions.indexOf(condition) < conditions.size() - 1) {
                    append(parentCondition.getJoinType().toString().toLowerCase(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
                }
            }
        }
    }
}
