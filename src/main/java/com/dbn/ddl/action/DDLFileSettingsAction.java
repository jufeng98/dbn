package com.dbn.ddl.action;

import com.dbn.options.ConfigId;
import com.dbn.options.action.ProjectSettingsOpenAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DDLFileSettingsAction extends ProjectSettingsOpenAction {
    public DDLFileSettingsAction() {
        super(ConfigId.DDL_FILES, false);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        super.update(e, project);
        e.getPresentation().setText("DDL File Settings...");
    }
}