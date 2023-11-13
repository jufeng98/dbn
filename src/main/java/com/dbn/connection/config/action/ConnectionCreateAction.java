package com.dbn.connection.config.action;

import com.dbn.connection.DatabaseType;
import com.dbn.connection.config.ConnectionConfigType;
import com.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConnectionCreateAction extends ConnectionSettingsAction {
    private DatabaseType databaseType;

    ConnectionCreateAction(@Nullable DatabaseType databaseType) {
        super(getName(databaseType), getIcon(databaseType));
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
            @NotNull Project project,
            @NotNull ConnectionBundleSettingsForm target) {

        DatabaseType databaseType = this.databaseType;
        ConnectionConfigType configType = ConnectionConfigType.BASIC;
        if (databaseType == null) {
            configType = ConnectionConfigType.CUSTOM;
            databaseType = DatabaseType.GENERIC;
        }

        target.createNewConnection(databaseType, configType);
    }
}
