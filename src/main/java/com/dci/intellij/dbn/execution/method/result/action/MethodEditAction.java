package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.editor.DatabaseFileEditorManager;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MethodEditAction extends AbstractMethodExecutionResultAction {
    public MethodEditAction() {
        super("Edit Method", Icons.OBEJCT_EDIT_SOURCE);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull MethodExecutionResult executionResult) {

        DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
        editorManager.connectAndOpenEditor(executionResult.getMethod(), null, true, true);
    }
}