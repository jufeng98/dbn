package com.dbn.browser.action;

import com.dbn.common.action.GroupPopupAction;
import com.dbn.common.icon.Icons;
import com.dbn.common.util.Actions;
import com.dbn.options.ConfigId;
import com.dbn.options.action.ProjectSettingsOpenAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class BrowserOptionsAction extends GroupPopupAction {
    public BrowserOptionsAction() {
        super("Options", "Options", Icons.ACTION_OPTIONS_MENU);
    }
    @Override
    protected AnAction[] getActions(AnActionEvent e) {
        return new AnAction[]{
                new AutoscrollToEditorAction(),
                new AutoscrollFromEditorAction(),
                Actions.SEPARATOR,
                new ConnectionFilterSettingsOpenAction(),
                Actions.SEPARATOR,
                new ProjectSettingsOpenAction(ConfigId.CONNECTIONS, false)
        };
    }
}
