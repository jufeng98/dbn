package com.dci.intellij.dbn.connection.config.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectionRemoveAction extends ConnectionSettingsAction {
    public ConnectionRemoveAction() {
        super("Remove connection", Icons.ACTION_REMOVE);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ConnectionBundleSettingsForm target) {

        target.removeSelectedConnections();
    }

    @Override
    protected void update(
            @NotNull AnActionEvent e,
            @NotNull Presentation presentation,
            @NotNull Project project,
            @Nullable ConnectionBundleSettingsForm target) {

        int length = target == null ? 0 : target.getSelectionSize();
        presentation.setEnabled(length > 0);
        presentation.setText(length == 1 ? "Remove Connections" : "Remove Connection");
    }
}
