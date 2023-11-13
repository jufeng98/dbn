package com.dci.intellij.dbn.execution.statement.result.action;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.execution.statement.result.StatementExecutionCursorResult;
import com.dci.intellij.dbn.execution.statement.result.ui.StatementExecutionResultForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static com.dci.intellij.dbn.common.dispose.Checks.isValid;

public class ExecutionResultFindDataAction extends AbstractExecutionResultAction {
    public ExecutionResultFindDataAction() {
        super("Find Data", Icons.ACTION_FIND);
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project, @NotNull StatementExecutionCursorResult executionResult) {
        StatementExecutionResultForm resultForm = executionResult.getForm();
        if (isValid(resultForm)) {
            resultForm.showSearchHeader();
        }
    }
}
