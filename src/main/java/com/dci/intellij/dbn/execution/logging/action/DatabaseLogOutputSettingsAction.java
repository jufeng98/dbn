package com.dci.intellij.dbn.execution.logging.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.logging.DatabaseLoggingResult;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DatabaseLogOutputSettingsAction extends AbstractDatabaseLoggingAction {
    public DatabaseLogOutputSettingsAction() {
        super("Settings", Icons.EXEC_RESULT_OPTIONS);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull DatabaseLoggingResult loggingResult) {
        ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);
        settingsManager.openProjectSettings(ConfigId.EXECUTION_ENGINE);
    }
}
