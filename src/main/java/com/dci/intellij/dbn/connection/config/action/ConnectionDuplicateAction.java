package com.dci.intellij.dbn.connection.config.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.connection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConnectionDuplicateAction extends ConnectionSettingsAction {
    public ConnectionDuplicateAction() {
        super("Duplicate Connection", Icons.ACTION_COPY);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull ConnectionBundleSettingsForm target) {

        target.duplicateSelectedConnection();
    }

    @Override
    protected void update(
            @NotNull AnActionEvent e,
            @NotNull Presentation presentation,
            @NotNull Project project,
            @Nullable ConnectionBundleSettingsForm target) {

        presentation.setEnabled(target != null && target.getSelectionSize() == 1);
    }
}
