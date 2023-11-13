package com.dbn.execution.explain.result.action;

import com.dbn.common.icon.Icons;
import com.dbn.execution.ExecutionManager;
import com.dbn.execution.explain.result.ExplainPlanResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ExplainPlanResultCloseAction extends AbstractExplainPlanResultAction {
    public ExplainPlanResultCloseAction() {
        super("Close", Icons.EXEC_RESULT_CLOSE);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ExplainPlanResult explainPlanResult) {
        ExecutionManager executionManager = ExecutionManager.getInstance(project);
        executionManager.removeResultTab(explainPlanResult);
    }
}
