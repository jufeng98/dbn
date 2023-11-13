package com.dci.intellij.dbn.execution.method.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.ExecutionManager;
import com.dci.intellij.dbn.execution.method.result.MethodExecutionResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ExecutionResultCloseAction extends AbstractMethodExecutionResultAction {
    public ExecutionResultCloseAction() {
        super("Close", Icons.EXEC_RESULT_CLOSE);
    }

    @Override
    protected void actionPerformed(
            @NotNull AnActionEvent e,
            @NotNull Project project,
            @NotNull MethodExecutionResult executionResult) {

        ExecutionManager.getInstance(project).removeResultTab(executionResult);
    }
}
