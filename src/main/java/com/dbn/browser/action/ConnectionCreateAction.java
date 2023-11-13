package com.dbn.browser.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.connection.DatabaseType;
import com.dbn.connection.config.ConnectionConfigType;
import com.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConnectionCreateAction extends ProjectAction {
    private final DatabaseType databaseType;

    ConnectionCreateAction(@Nullable DatabaseType databaseType) {
        super(getName(databaseType), null, getIcon(databaseType));
        this.databaseType = databaseType;
    }

    private static Icon getIcon(@Nullable DatabaseType databaseType) {
        return databaseType == null ? null : databaseType.getIcon();
    }

    private static String getName(@Nullable DatabaseType databaseType) {
        return databaseType == null ? "Custom..." : databaseType.getName();
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project) {

        ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);

        DatabaseType databaseType = this.databaseType;
        ConnectionConfigType configType = ConnectionConfigType.BASIC;
        if (databaseType == null) {
            configType = ConnectionConfigType.CUSTOM;
            databaseType = DatabaseType.GENERIC;
        }
        settingsManager.createConnection(databaseType, configType);
    }
}
