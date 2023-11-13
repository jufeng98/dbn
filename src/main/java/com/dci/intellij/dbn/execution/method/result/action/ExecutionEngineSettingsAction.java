package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.options.ConfigId;
import com.dci.intellij.dbn.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ExecutionEngineSettingsAction extends ProjectAction {

    public ExecutionEngineSettingsAction() {
        super("Settings", null, Icons.EXEC_RESULT_OPTIONS);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);
        settingsManager.openProjectSettings(ConfigId.EXECUTION_ENGINE);
    }
}
