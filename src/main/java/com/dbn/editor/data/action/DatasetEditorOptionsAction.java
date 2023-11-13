package com.dbn.editor.data.action;

import com.dbn.common.action.GroupPopupAction;
import com.dbn.common.icon.Icons;
import com.dbn.common.util.Actions;
import com.dbn.options.ConfigId;
import com.dbn.options.action.ProjectSettingsOpenAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class DatasetEditorOptionsAction extends GroupPopupAction {
    public DatasetEditorOptionsAction() {
        super("Options", "Options", Icons.ACTION_OPTIONS_MENU);
    }
    @Override
    protected AnAction[] getActions(AnActionEvent e) {
        return new AnAction[]{
                new DataSortingOpenAction(),
                new ColumnSetupOpenAction(),
                Actions.SEPARATOR,
                new ProjectSettingsOpenAction(ConfigId.DATA_EDITOR, false)
        };
    }
}
