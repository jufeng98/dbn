package com.dbn.object.filter.name.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.object.filter.name.ui.ObjectNameFilterSettingsForm;

import javax.swing.*;
import javax.swing.tree.TreePath;

public abstract class AbstractObjectFilterAction extends ProjectAction {
    ObjectNameFilterSettingsForm settingsForm;

    AbstractObjectFilterAction(String text, Icon icon, ObjectNameFilterSettingsForm settingsForm) {
        super(text, null, icon);
        this.settingsForm = settingsForm;
    }

    protected Object getSelection() {
        TreePath selectionPath = getFiltersTree().getSelectionPath();
        return selectionPath == null ? null : selectionPath.getLastPathComponent();
    }

    JTree getFiltersTree() {
        return settingsForm.getFiltersTree();
    }
}
