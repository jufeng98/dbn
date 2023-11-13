package com.dbn.browser.action;

import com.dbn.common.action.DataKeys;
import com.dbn.common.action.GroupPopupAction;
import com.dbn.common.icon.Icons;
import com.dbn.common.util.Actions;
import com.dbn.connection.DatabaseType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConnectionCreateDropdownAction extends GroupPopupAction {
    private final AnAction[] actions = new AnAction[] {
            new ConnectionCreateAction(DatabaseType.ORACLE),
            new ConnectionCreateAction(DatabaseType.MYSQL),
            new ConnectionCreateAction(DatabaseType.POSTGRES),
            new ConnectionCreateAction(DatabaseType.SQLITE),
            new ConnectionCreateAction(null),
            Actions.SEPARATOR,
            new TnsNamesImportAction()
    };

    public ConnectionCreateDropdownAction() {
        super("New Connection", null, Icons.ACTION_ADD_MORE);
    }

    public ConnectionCreateDropdownAction(String name, @Nullable String groupTitle, @Nullable Icon icon) {
        super(name, groupTitle, icon);
    }

    @Override
    public DataProvider getDataProvider(AnActionEvent e) {
        return e.getData((DataKeys.CONNECTION_BUNDLE_SETTINGS));
    }

    @Override
    protected AnAction[] getActions(AnActionEvent e) {
        return actions;
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        presentation.setText("New Connection");
        presentation.setEnabled(true);
    }
}
