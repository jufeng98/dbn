package com.dci.intellij.dbn.execution.explain.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.common.ui.StatementViewerPopup;
import com.dci.intellij.dbn.execution.explain.result.ExplainPlanResult;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ExplainPlanResultViewStatementAction extends AbstractExplainPlanResultAction {
    public ExplainPlanResultViewStatementAction() {
        super("View SQL statement", Icons.EXEC_RESULT_VIEW_STATEMENT);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull ExplainPlanResult explainPlanResult) {
        StatementViewerPopup statementViewer = new StatementViewerPopup(explainPlanResult);
        statementViewer.show((Component) e.getInputEvent().getSource());
    }
}
