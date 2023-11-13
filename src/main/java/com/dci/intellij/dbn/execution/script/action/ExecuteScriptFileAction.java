package com.dci.intellij.dbn.execution.script.action;

import com.dci.intellij.dbn.common.action.Lookups;
import com.dci.intellij.dbn.common.action.ProjectAction;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.script.ScriptExecutionManager;
import com.dci.intellij.dbn.language.psql.PSQLFileType;
import com.dci.intellij.dbn.language.sql.SQLFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class ExecuteScriptFileAction extends ProjectAction {

    public ExecuteScriptFileAction() {
        super("Execute SQL Script", null, Icons.EXECUTE_SQL_SCRIPT);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        if (isAvailableFor(virtualFile)) {
            ScriptExecutionManager scriptExecutionManager = ScriptExecutionManager.getInstance(project);
            scriptExecutionManager.executeScript(virtualFile);
        }
    }

    private boolean isAvailableFor(VirtualFile virtualFile) {
        return virtualFile != null && (
                virtualFile.getFileType() == SQLFileType.INSTANCE ||
                virtualFile.getFileType() == PSQLFileType.INSTANCE);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        Presentation presentation = e.getPresentation();
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        presentation.setVisible(isAvailableFor(virtualFile));
        presentation.setIcon(Icons.EXECUTE_SQL_SCRIPT);
        presentation.setText("Execute SQL Script");
    }
}
